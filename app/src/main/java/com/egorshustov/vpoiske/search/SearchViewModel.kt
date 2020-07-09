package com.egorshustov.vpoiske.search

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.egorshustov.vpoiske.analytics.VPoiskeAnalytics
import com.egorshustov.vpoiske.domain.users.GetUsersUseCase
import com.egorshustov.vpoiske.util.*

class SearchViewModel @ViewModelInject constructor(
    getUsersUseCase: GetUsersUseCase,
    sharedPreferences: SharedPreferences,
    private val vPoiskeAnalytics: VPoiskeAnalytics
) : ViewModel() {

    var currentColumnCount by DelegatedPreference(
        sharedPreferences,
        PREF_KEY_CURRENT_COLUMN_COUNT,
        DEFAULT_COLUMN_COUNT
    )
        private set

    private val _currentColumnCountChanged = MutableLiveData<Int>()
    val currentColumnCountChanged: LiveData<Int> = _currentColumnCountChanged

    private var currentSearchId = MutableLiveData<Long?>(null)

    val currentSearchUsers = currentSearchId.switchMap {
        getUsersUseCase(it).map {
            isLoading.value = false
            it
        }
    }

    private val _openUserDetails = MutableLiveData<Event<Long>>()
    val openUserDetails: LiveData<Event<Long>> = _openUserDetails

    val isLoading = MutableLiveData(true)

    fun onSearchFragmentViewCreated() {
        vPoiskeAnalytics.onPastSearchScreenOpened()
    }

    fun onCurrentSearchIdObtained(searchId: Long) {
        currentSearchId.value = searchId
    }

    fun openUser(userId: Long) {
        vPoiskeAnalytics.onUserClicked()
        _openUserDetails.value = Event(userId)
    }

    fun onItemChangeViewClicked() {
        currentColumnCount =
            if (currentColumnCount.dec() == 0) MAX_COLUMN_COUNT else currentColumnCount.dec()
        _currentColumnCountChanged.value = currentColumnCount
        vPoiskeAnalytics.onChangeUsersViewClicked(currentColumnCount)
    }
}