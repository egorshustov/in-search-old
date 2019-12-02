package com.egorshustov.vpoiske.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.SearchesRepository
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val searchesRepository: SearchesRepository
) : BaseViewModel<MainState>(MainState()) {

    private val users: LiveData<List<User>> = usersRepository.getLiveUsers()

    val currentSearchUsers: LiveData<List<User>> = Transformations.map(users) {
        it.filter { it.searchId == newSearchId }
    }

    private val _openUserEvent = MutableLiveData<Event<Long>>()
    val openUserEvent: LiveData<Event<Long>> = _openUserEvent

    private val _openNewSearch = MutableLiveData<Event<Unit>>()
    val openNewSearch: LiveData<Event<Unit>> = _openNewSearch

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
    private var newSearchId: Long? = null

    @Volatile
    private var searchJob: Job? = null

    init {
        Timber.d("%s init", toString())
    }

    fun onSearchButtonClicked(searchId: Long) {
        searchJob = viewModelScope.launch {
            val search = searchesRepository.getSearch(searchId) ?: return@launch
            newSearchId = searchId
            searchState.value = SearchState.IN_PROGRESS
            currentUnixSeconds.let {
                searchesRepository.updateSearchStartUnixSeconds(searchId, it)
                search.startUnixSeconds = it
            }
            while (true) {
                val randomDay = (1..MAX_DAYS_IN_MONTH).random()
                val randomMonth = (1..MONTHS_IN_YEAR).random()
                addMessageToLog("Поиск людей от $randomDay.$randomMonth")
                sendSearchUsersRequest(randomDay, randomMonth, search)
                delay(PAUSE_DELAY_IN_MILLIS)
            }
        }
    }

    private suspend fun sendSearchUsersRequest(birthDay: Int, birthMonth: Int, search: Search) {
        when (val searchUsersResult = usersRepository.searchUsers(
            search.countryId,
            search.cityId,
            search.ageFrom,
            search.ageTo,
            birthDay,
            birthMonth,
            if (search.friendsMinCount == null && search.friendsMaxCount == null) SEARCH_USERS_FRIENDS_LIMIT_NOT_SET_FIELDS else SEARCH_USERS_FRIENDS_LIMIT_SET_FIELDS,
            search.relation,
            search.sex
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
                    handleSearchUsersSuccess(searchUserResponseList, search)
                }
            }
            is Result.Error -> {
                handleError(searchUsersResult.exception)
                //todo delay and resend only if 'frequent requests exception'
                delay(ERROR_DELAY_IN_MILLIS)
                sendSearchUsersRequest(birthDay, birthMonth, search)
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
            addMessageToLog("Людей отфильтровано: ${filteredSearchUserResponseList.size}")
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
                val addedUserIdList = usersRepository.insertUsers(userList)
                foundUsersCount += addedUserIdList.size
                if (foundUsersCount >= search.foundUsersLimit) stopSearch()
            }
        }
    }

    private suspend fun sendGetUserRequest(userId: Long, search: Search) {
        val friendsMinCount = search.friendsMinCount
        val friendsMaxCount = search.friendsMaxCount
        if (friendsMinCount == null || friendsMaxCount == null) return

        when (val getUserResult = usersRepository.getUser(userId)) {
            is Result.Success -> {
                val user = getUserResult.data.toEntity()
                val isFriendsCountAcceptable =
                    user.friends in friendsMinCount..friendsMaxCount
                if (isFriendsCountAcceptable) {
                    val addedUserId =
                        usersRepository.insertUser(user.apply {
                            searchId = search.id
                            foundUnixMillis = currentUnixMillis
                        })
                    if (addedUserId != NO_VALUE.toLong()) ++foundUsersCount
                    if (foundUsersCount >= search.foundUsersLimit) stopSearch()
                }
            }
            is Result.Error -> {
                handleError(getUserResult.exception)
                //todo delay and resend only if 'frequent requests exception'
                delay(ERROR_DELAY_IN_MILLIS)
                sendGetUserRequest(userId, search)
            }
        }
    }

    private fun handleError(exception: Exception) {
        //todo change method functionality
        Timber.d("exception: $exception")
        addMessageToLog("Ошибка запроса: $exception")
    }

    private fun addMessageToLog(message: String) {
        _logMessage.value = Event(message)
    }

    private fun stopSearch() {
        searchJob?.cancel()
        searchState.value = SearchState.INACTIVE
    }

    fun openUser(userId: Long) {
        _openUserEvent.value = Event(userId)
    }

    fun changeSearchState() {
        if (searchState.value == SearchState.INACTIVE) _openNewSearch.value = Event(Unit)
        else stopSearch()
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}