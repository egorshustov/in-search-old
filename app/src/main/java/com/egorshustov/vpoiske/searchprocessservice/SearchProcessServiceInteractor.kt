package com.egorshustov.vpoiske.searchprocessservice

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egorshustov.vpoiske.analytics.VPoiskeAnalytics
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.domain.searches.GetSearchUseCase
import com.egorshustov.vpoiske.domain.searches.SaveSearchStartUnixSecondsUseCase
import com.egorshustov.vpoiske.domain.users.GetUserUseCase
import com.egorshustov.vpoiske.domain.users.SaveUserUseCase
import com.egorshustov.vpoiske.domain.users.SaveUsersUseCase
import com.egorshustov.vpoiske.domain.users.SearchUsersUseCase
import com.egorshustov.vpoiske.util.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@ServiceScoped
class SearchProcessServiceInteractor @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val saveUsersUseCase: SaveUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getSearchUseCase: GetSearchUseCase,
    private val saveSearchStartUnixSecondsUseCase: SaveSearchStartUnixSecondsUseCase,
    private val vPoiskeAnalytics: VPoiskeAnalytics,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _foundUsersCountUpdated = MutableLiveData<Int>()
    val foundUsersCountUpdated: LiveData<Int> = _foundUsersCountUpdated

    private var foundUsersCount: Int = 0
        set(value) {
            field = value
            if (Looper.myLooper() == Looper.getMainLooper()) {
                _foundUsersCountUpdated.value = value
            } else {
                _foundUsersCountUpdated.postValue(value)
            }
        }

    var foundUsersLimit: Int? = null
        private set

    suspend fun onSearchStarted(searchId: Long) {
        foundUsersCount = 0
        withContext(ioDispatcher) {
            val search = getSearchUseCase(searchId) ?: return@withContext
            foundUsersLimit = search.foundUsersLimit
            currentUnixSeconds.let {
                saveSearchStartUnixSecondsUseCase(searchId, it)
                search.startUnixSeconds = it
            }
            while (isActive) {
                val randomDay = (1..MAX_DAYS_IN_MONTH).random()
                val randomMonth = (1..MONTHS_IN_YEAR).random()
                Timber.d("Поиск людей от $randomDay.$randomMonth")
                sendSearchUsersRequest(randomDay, randomMonth, search)
                delay(PAUSE_DELAY_IN_MILLIS)
            }
        }
    }

    private suspend fun sendSearchUsersRequest(birthDay: Int, birthMonth: Int, search: Search) {
        when (val searchUsersResult = searchUsersUseCase(
            search.countryId,
            search.cityId,
            search.ageFrom,
            search.ageTo,
            birthDay,
            birthMonth,
            if (search.friendsMinCount == null && search.friendsMaxCount == null) {
                SEARCH_USERS_FRIENDS_LIMIT_NOT_SET_FIELDS
            } else {
                SEARCH_USERS_FRIENDS_LIMIT_SET_FIELDS
            },
            search.homeTown?.takeUnless { it.isBlank() },
            search.relation,
            search.sex
        )) {
            is Result.Success -> {
                @Suppress("USELESS_CAST") val searchUsersInnerResponse =
                    searchUsersResult.data as SearchUsersInnerResponse
                val count = searchUsersInnerResponse.count
                Timber.d("Людей всего: $count")
                //todo if count > 1000 split request
                val searchUserResponseList = searchUsersInnerResponse.searchUserResponseList
                Timber.d("Людей получено: ${searchUserResponseList?.size}")
                if (!searchUserResponseList.isNullOrEmpty()) {
                    handleSearchUsersSuccess(searchUserResponseList, search)
                }
            }
            is Result.Error -> {
                Timber.e(searchUsersResult.exception)
                FirebaseCrashlytics.getInstance().recordException(searchUsersResult.exception)
                if (searchUsersResult.exception.needToWait()) {
                    delay(ERROR_DELAY_IN_MILLIS)
                    sendSearchUsersRequest(birthDay, birthMonth, search)
                } else {
                    vPoiskeAnalytics.errorOccurred(
                        searchUsersResult.getString(),
                        searchUsersResult.exception.cause?.toString(),
                        searchUsersResult.exception.vkErrorCode
                    )
                    currentCoroutineContext().cancel()
                    _message.postValue(Event(searchUsersResult.getString()))
                }
            }
        }
    }

    private suspend fun handleSearchUsersSuccess(
        searchUserResponseList: List<SearchUserResponse>,
        search: Search
    ) {
        with(search) {
            val filteredSearchUserResponseList = searchUserResponseList.filter {
                val isNotClosed = it.isClosed == false
                val isFollowersCountAcceptable =
                    it.followersCount in followersMinCount..followersMaxCount
                val isInDaysInterval = startUnixSeconds - (it.lastSeen?.timeUnixSeconds
                    ?: 0) < daysInterval * SECONDS_IN_DAY
                val phoneCheckPassed = if (withPhoneOnly) it.hasCorrectPhone else true
                isNotClosed && isFollowersCountAcceptable && isInDaysInterval && phoneCheckPassed
            }
            Timber.d("Людей отфильтровано: ${filteredSearchUserResponseList.size}")
            if (filteredSearchUserResponseList.isNullOrEmpty()) return

            if (friendsMinCount != null && friendsMaxCount != null) {
                filteredSearchUserResponseList.forEach {
                    it.id?.let { userId ->
                        //todo do not send request if user is already in DB
                        delay(PAUSE_DELAY_IN_MILLIS)
                        sendGetUserRequest(userId, search)
                    }
                }
            } else {
                val userList = filteredSearchUserResponseList.map { it.toEntity() }
                    .apply {
                        forEach {
                            it.searchId = search.id
                            it.foundUnixMillis = currentUnixMillis
                        }
                    }
                val addedUserIdList = saveUsersUseCase(userList)
                foundUsersCount += addedUserIdList.size
                if (foundUsersCount >= search.foundUsersLimit) currentCoroutineContext().cancel()
            }
        }
    }

    private suspend fun sendGetUserRequest(userId: Long, search: Search) {
        val friendsMinCount = search.friendsMinCount
        val friendsMaxCount = search.friendsMaxCount
        if (friendsMinCount == null || friendsMaxCount == null) return

        when (val getUserResult = getUserUseCase(userId)) {
            is Result.Success -> {
                val user = getUserResult.data.toEntity()
                val isFriendsCountAcceptable =
                    user.friends in friendsMinCount..friendsMaxCount
                val isDesiredRelation =
                    !(search.relation != null && user.relation != search.relation)
                if (isFriendsCountAcceptable && isDesiredRelation) {
                    val addedUserId = saveUserUseCase(user.apply {
                        searchId = search.id
                        foundUnixMillis = currentUnixMillis
                    })
                    if (addedUserId != NO_VALUE.toLong()) ++foundUsersCount
                    if (foundUsersCount >= search.foundUsersLimit) currentCoroutineContext().cancel()
                }
            }
            is Result.Error -> {
                Timber.e(getUserResult.exception)
                FirebaseCrashlytics.getInstance().recordException(getUserResult.exception)
                if (getUserResult.exception.needToWait()) {
                    delay(ERROR_DELAY_IN_MILLIS)
                    sendGetUserRequest(userId, search)
                } else {
                    vPoiskeAnalytics.errorOccurred(
                        getUserResult.getString(),
                        getUserResult.exception.cause?.toString(),
                        getUserResult.exception.vkErrorCode
                    )
                    currentCoroutineContext().cancel()
                    _message.postValue(Event(getUserResult.getString()))
                }
            }
        }
    }

    fun onStopSearchClicked() {
        vPoiskeAnalytics.stopSearchClicked(fromNotification = true)
    }

    fun onSearchCompleted() {
        vPoiskeAnalytics.searchCompleted(
            foundUsersCountUpdated.value ?: 0,
            foundUsersLimit ?: 0
        )
    }
}