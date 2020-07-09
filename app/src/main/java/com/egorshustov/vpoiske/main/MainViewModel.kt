package com.egorshustov.vpoiske.main

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.egorshustov.vpoiske.analytics.VPoiskeAnalytics
import com.egorshustov.vpoiske.domain.searches.GetLastSearchIdUseCase
import com.egorshustov.vpoiske.domain.users.GetUsersUseCase
import com.egorshustov.vpoiske.util.*

class MainViewModel @ViewModelInject constructor(
    getUsersUseCase: GetUsersUseCase,
    getLastSearchIdUseCase: GetLastSearchIdUseCase,
    sharedPreferences: SharedPreferences,
    private val vPoiskeAnalytics: VPoiskeAnalytics
) : ViewModel() {

    var currentThemeId by DelegatedPreference(
        sharedPreferences,
        PREF_KEY_CURRENT_THEME_ID,
        VPoiskeTheme.LIGHT_THEME.id
    )
        private set

    var currentColumnCount by DelegatedPreference(
        sharedPreferences,
        PREF_KEY_CURRENT_COLUMN_COUNT,
        DEFAULT_COLUMN_COUNT
    )
        private set

    val isLoading = MutableLiveData(false)

    private val _currentColumnCountChanged = MutableLiveData<Int>()
    val currentColumnCountChanged: LiveData<Int> = _currentColumnCountChanged

    val lastSearchId: LiveData<Long?> = getLastSearchIdUseCase()

    val currentSearchUsers = lastSearchId.switchMap { lastSearchId ->
        getUsersUseCase(lastSearchId).map {
            if (it.isNotEmpty()) isLoading.value = false
            it
        }
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

    fun onNavChangeThemeClicked() {
        val currentTheme =
            VPoiskeTheme.values().find { it.id == currentThemeId } ?: VPoiskeTheme.LIGHT_THEME
        val nextTheme = currentTheme.getNext()
        vPoiskeAnalytics.onChangeAppThemeClicked(nextTheme.name)
        currentThemeId = nextTheme.id
    }

    fun onSearchButtonClicked(searchId: Long) {
        isLoading.value = true
        searchState.value = SearchProcessState.IN_PROGRESS
        _startNewSearch.value = Event(searchId)
    }

    private fun stopSearch() {
        vPoiskeAnalytics.stopSearchClicked(fromNotification = false)
        _stopSearch.value = Event(Unit)
        isLoading.value = false
        searchState.value = SearchProcessState.INACTIVE
    }

    fun openUser(userId: Long) {
        vPoiskeAnalytics.onUserClicked()
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
        currentColumnCount =
            if (currentColumnCount.dec() == 0) MAX_COLUMN_COUNT else currentColumnCount.dec()
        _currentColumnCountChanged.value = currentColumnCount
        vPoiskeAnalytics.onChangeUsersViewClicked(currentColumnCount)
    }
}