package com.egorshustov.vpoiske.main

import android.content.SharedPreferences
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

class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val searchesRepository: SearchesRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    var currentThemeId by DelegatedPreference(sharedPreferences, PREF_KEY_CURRENT_THEME_ID, VPoiskeTheme.LIGHT_THEME.id)
        private set

    var currentSpanCount by DelegatedPreference(sharedPreferences, PREF_KEY_CURRENT_SPAN_COUNT, DEFAULT_SPAN_COUNT)
        private set



    private val _currentSpanCountChanged = MutableLiveData<Int>()
    val currentSpanCountChanged: LiveData<Int> = _currentSpanCountChanged

    private val users: LiveData<List<User>> = usersRepository.getLiveUsers()

    val lastSearchId = searchesRepository.getLiveLastSearchId()

    val currentSearchUsers: LiveData<List<User>> = Transformations.map(users) { users ->
        isLoading.value = false
        users.filter { it.searchId == lastSearchId.value }
    }

    private val _openUserEvent = MutableLiveData<Event<Long>>()
    val openUserEvent: LiveData<Event<Long>> = _openUserEvent

    private val _openNewSearch = MutableLiveData<Event<Unit>>()
    val openNewSearch: LiveData<Event<Unit>> = _openNewSearch

    val searchState = MutableLiveData(SearchProcessState.INACTIVE)

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    val isLoading = MutableLiveData(true)

    private var foundUsersCount: Int = 0

    private var searchJob: Job? = null

    init {
        Timber.d("%s init", toString())
    }

    fun onNavChangeThemeClicked() {
        val currentTheme =
            VPoiskeTheme.values().find { it.id == currentThemeId } ?: VPoiskeTheme.LIGHT_THEME
        currentThemeId = currentTheme.getNext().id
    }

    fun onSearchButtonClicked(searchId: Long) {
        isLoading.value = true
        searchJob = viewModelScope.launch {
            val search = searchesRepository.getSearch(searchId) ?: return@launch
            //lastSearchId.value = searchId
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
        isLoading.value = false
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

    fun onItemChangeViewClicked() {
        currentSpanCount =
            if (currentSpanCount.dec() == 0) MAX_SPAN_COUNT else currentSpanCount.dec()
        _currentSpanCountChanged.value = currentSpanCount
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}