package com.egorshustov.vpoiske.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel<MainState>(MainState()) {

    enum class SearchState {
        INACTIVE,
        IN_PROGRESS
    }

    val searchState = MutableLiveData<SearchState>(SearchState.INACTIVE)

    private val _logMessage = MutableLiveData<Event<String>>()
    val logMessage: LiveData<Event<String>> = _logMessage

    @Volatile
    private var foundUsersCount: Int = 0

    @Volatile
    private var newSearchId: Int? = null

    init {
        Timber.d("%s init", toString())
    }

    fun onSearchButtonClicked(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        relation: Int?,
        sex: Int,
        withPhoneOnly: Boolean,
        foundedUsersLimit: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int,
        daysInterval: Int
    ) = viewModelScope.launch {
        val searchStartUnixSeconds = (System.currentTimeMillis() / MILLIS_IN_SECOND).toInt()
        while (true) {
            val randomDay = (1..31).random()
            val randomMonth = (1..12).random()
            addMessageToLog("Поиск людей от $randomDay.$randomMonth")
            delay(500)
            sendSearchUsersRequest(
                searchStartUnixSeconds,
                countryId,
                cityId,
                ageFrom,
                ageTo,
                randomDay,
                randomMonth,
                relation,
                sex,
                withPhoneOnly,
                foundedUsersLimit,
                friendsMinCount,
                friendsMaxCount,
                followersMinCount,
                followersMaxCount,
                daysInterval
            )
        }
    }

    private suspend fun sendSearchUsersRequest(
        searchStartUnixSeconds: Int,
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        birthDay: Int,
        birthMonth: Int,
        relation: Int?,
        sex: Int,
        withPhoneOnly: Boolean,
        foundedUsersLimit: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int,
        daysInterval: Int
    ) {
        when (val searchUsersResult = usersRepository.searchUsers(
            countryId,
            cityId,
            ageFrom,
            ageTo,
            birthDay,
            birthMonth,
            if (friendsMinCount == null && friendsMaxCount == null) SEARCH_USERS_FRIENDS_LIMIT_NOT_SET_FIELDS else SEARCH_USERS_FRIENDS_LIMIT_SET_FIELDS,
            relation,
            sex
        )) {
            is Result.Success -> {
                val searchUsersInnerResponse =
                    searchUsersResult.data as SearchUsersInnerResponse
                val count = searchUsersInnerResponse.count
                addMessageToLog("Людей всего: $count")
                //todo if count > 1000 split request
                val searchUserResponseList = searchUsersInnerResponse.searchUserResponseList
                addMessageToLog("Людей получено: ${searchUserResponseList?.size}")
                if (!searchUserResponseList.isNullOrEmpty()) {
                    handleSearchUsersSuccess(
                        searchUserResponseList,
                        searchStartUnixSeconds,
                        withPhoneOnly,
                        foundedUsersLimit,
                        friendsMinCount,
                        friendsMaxCount,
                        followersMinCount,
                        followersMaxCount,
                        daysInterval
                    )
                }

            }
            is Result.Error -> {
                handleError(searchUsersResult.exception)
                //todo delay and resend only if 'frequent requests exception'
                delay(3000)
                sendSearchUsersRequest(
                    searchStartUnixSeconds,
                    countryId,
                    cityId,
                    ageFrom,
                    ageTo,
                    birthDay,
                    birthMonth,
                    relation,
                    sex,
                    withPhoneOnly,
                    foundedUsersLimit,
                    friendsMinCount,
                    friendsMaxCount,
                    followersMinCount,
                    followersMaxCount,
                    daysInterval
                )
            }
        }
    }

    private suspend fun handleSearchUsersSuccess(
        searchUserResponseList: List<SearchUserResponse>,
        searchStartUnixSeconds: Int,
        withPhoneOnly: Boolean,
        foundedUsersLimit: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int,
        daysInterval: Int
    ) {
        val filteredSearchUserResponseList = searchUserResponseList.filter {
            val isNotClosed = it.isClosed == false
            val isFollowersCountAcceptable =
                it.followersCount in followersMinCount..followersMaxCount
            val isInDaysInterval = searchStartUnixSeconds - (it.lastSeen?.timeUnixSeconds
                ?: 0) < daysInterval * SECONDS_IN_DAY
            val phoneCheckPassed = if (withPhoneOnly) it.hasCorrectPhone() else true
            isNotClosed && isFollowersCountAcceptable && isInDaysInterval && phoneCheckPassed
        }
        addMessageToLog("Людей отфильтровано: ${filteredSearchUserResponseList.size}")

        if (friendsMinCount != null && friendsMaxCount != null) {
            filteredSearchUserResponseList.forEach {
                it.id?.let { userId ->
                    //todo do not send request if user is already in DB
                    delay(500)
                    sendGetUserRequest(userId, friendsMinCount, friendsMaxCount)
                }
            }
        } else {
            //todo check what if filteredSearchUserResponseList is empty
            val userList = filteredSearchUserResponseList.map { it.toEntity() }
            if (newSearchId == null) {
                //todo create search
            }
            //todo insert all in DB with ignore annotation
        }
    }

    private suspend fun sendGetUserRequest(
        userId: Int,
        friendsMinCount: Int,
        friendsMaxCount: Int
    ) {
        when (val getUserResult = usersRepository.getUser(userId)) {
            is Result.Success -> {
                val user = getUserResult.data.toEntity()
                val isFriendsCountAcceptable = user.friends in friendsMinCount..friendsMaxCount

                if (isFriendsCountAcceptable) {
                    if (newSearchId == null) {
                        //todo create search
                    }
                    //todo insert in DB with searchId
                }

            }
            is Result.Error -> {
                handleError(getUserResult.exception)
                //todo delay and resend only if 'frequent requests exception'
                delay(3000)
                sendGetUserRequest(userId, friendsMinCount, friendsMaxCount)
            }
        }
    }

    private fun handleError(exception: Exception) {
        Timber.d("exception: $exception")
        addMessageToLog("Ошибка запроса: $exception")
    }

    private fun addMessageToLog(message: String) {
        _logMessage.value = Event(message)
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}