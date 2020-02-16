package com.egorshustov.vpoiske.searchprocess

import androidx.lifecycle.*
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

class SearchProcessViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val searchesRepository: SearchesRepository
) : ViewModel() {

    var currentTheme = VPoiskeTheme.LIGHT_THEME
        private set

    private val users: LiveData<List<User>> = usersRepository.getLiveUsers()

    val currentSearchUsers: LiveData<List<User>> = Transformations.map(users) {
        it.filter { it.searchId == newSearchId }
    }

    private val _openUserEvent = MutableLiveData<Event<Long>>()
    val openUserEvent: LiveData<Event<Long>> = _openUserEvent

    private val _openNewSearch = MutableLiveData<Event<Unit>>()
    val openNewSearch: LiveData<Event<Unit>> = _openNewSearch

    val searchState = MutableLiveData<SearchProcessState>(SearchProcessState.INACTIVE)

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private var foundUsersCount: Int = 0

    private var newSearchId: Long? = null

    private var searchJob: Job? = null

    init {
        Timber.d("%s init", toString())
    }

    fun onNavChangeThemeClicked() {
        currentTheme = currentTheme.getNext()
    }

    fun onSearchButtonClicked(searchId: Long) {
        searchJob = viewModelScope.launch {
            val search = searchesRepository.getSearch(searchId) ?: return@launch
            newSearchId = searchId
            searchState.value = SearchProcessState.IN_PROGRESS
            currentUnixSeconds.let {
                searchesRepository.updateSearchStartUnixSeconds(searchId, it)
                search.startUnixSeconds = it
            }
            while (true) {
                val randomDay = (1..MAX_DAYS_IN_MONTH).random()
                val randomMonth = (1..MONTHS_IN_YEAR).random()
                Timber.d("Поиск людей от $randomDay.$randomMonth")
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
            if (search.friendsMinCount == null && search.friendsMaxCount == null) {
                SEARCH_USERS_FRIENDS_LIMIT_NOT_SET_FIELDS
            } else {
                SEARCH_USERS_FRIENDS_LIMIT_SET_FIELDS
            },
            search.relation,
            search.sex
        )) {
            is Result.Success -> {
                val searchUsersInnerResponse =
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
                Timber.d("exception: ${searchUsersResult.exception}")
                if (searchUsersResult.exception.needToWait()) {
                    delay(ERROR_DELAY_IN_MILLIS)
                    sendSearchUsersRequest(birthDay, birthMonth, search)
                } else {
                    stopSearch()
                    _message.value = Event(searchUsersResult.getString())
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
                Timber.d("exception: ${getUserResult.exception}")
                if (getUserResult.exception.needToWait()) {
                    delay(ERROR_DELAY_IN_MILLIS)
                    sendGetUserRequest(userId, search)
                } else {
                    stopSearch()
                    _message.value = Event(getUserResult.getString())
                }
            }
        }
    }

    private fun stopSearch() {
        searchJob?.cancel()
        searchState.value = SearchProcessState.INACTIVE
    }

    fun openUser(userId: Long) {
        _openUserEvent.value = Event(userId)
    }

    fun onFabStartClicked() {
        changeSearchState()
    }

    private fun changeSearchState() {
        if (searchState.value == SearchProcessState.INACTIVE) _openNewSearch.value = Event(Unit)
        else stopSearch()
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}