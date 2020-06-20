package com.egorshustov.vpoiske.main

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.IBinder
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.SearchesRepository
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.searchprocessservice.SearchProcessService
import com.egorshustov.vpoiske.util.*
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    usersRepository: UsersRepository,
    searchesRepository: SearchesRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    var currentThemeId by DelegatedPreference(
        sharedPreferences,
        PREF_KEY_CURRENT_THEME_ID,
        VPoiskeTheme.LIGHT_THEME.id
    )
        private set

    var currentSpanCount by DelegatedPreference(
        sharedPreferences,
        PREF_KEY_CURRENT_SPAN_COUNT,
        DEFAULT_SPAN_COUNT
    )
        private set

    private val _currentSpanCountChanged = MutableLiveData<Int>()
    val currentSpanCountChanged: LiveData<Int> = _currentSpanCountChanged

    private val users: LiveData<List<User>> = usersRepository.getLiveUsers()

    val lastSearchId = searchesRepository.getLiveLastSearchId()

    val currentSearchUsers: LiveData<List<User>> = Transformations.map(users) { users ->
        isLoading.value = false
        users.filter { it.searchId == lastSearchId.value }
    }

    private val _openUserDetails = MutableLiveData<Event<Long>>()
    val openUserDetails: LiveData<Event<Long>> = _openUserDetails

    private val _openSearchParams = MutableLiveData<Event<Unit>>()
    val openSearchParams: LiveData<Event<Unit>> = _openSearchParams

    private val _startNewSearch = MutableLiveData<Event<Long>>()
    val startNewSearch: LiveData<Event<Long>> = _startNewSearch

    private val _stopSearch = MutableLiveData<Event<Unit>>()
    val stopSearch: LiveData<Event<Unit>> = _stopSearch

    val searchState = MutableLiveData(SearchProcessState.INACTIVE)

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    val isLoading = MutableLiveData(true)

    fun onNavChangeThemeClicked() {
        val currentTheme =
            VPoiskeTheme.values().find { it.id == currentThemeId } ?: VPoiskeTheme.LIGHT_THEME
        currentThemeId = currentTheme.getNext().id
    }

    fun onSearchButtonClicked(searchId: Long) {
        isLoading.value = true
        searchState.value = SearchProcessState.IN_PROGRESS
        _startNewSearch.value = Event(searchId)
    }

    private fun stopSearch() {
        _stopSearch.value = Event(Unit)
        isLoading.value = false
        searchState.value = SearchProcessState.INACTIVE
    }

    fun openUser(userId: Long) {
        _openUserDetails.value = Event(userId)
    }

    fun onFabStartClicked() {
        changeSearchState()
    }

    private fun changeSearchState() {
        if (searchState.value == SearchProcessState.INACTIVE) _openSearchParams.value = Event(Unit)
        else stopSearch()
    }

    fun onItemChangeViewClicked() {
        currentSpanCount =
            if (currentSpanCount.dec() == 0) MAX_SPAN_COUNT else currentSpanCount.dec()
        _currentSpanCountChanged.value = currentSpanCount
    }
}