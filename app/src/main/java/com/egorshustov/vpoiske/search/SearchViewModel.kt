package com.egorshustov.vpoiske.search

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.egorshustov.vpoiske.domain.users.GetUsersUseCase
import com.egorshustov.vpoiske.util.*

class SearchViewModel @ViewModelInject constructor(
    getUsersUseCase: GetUsersUseCase,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    var currentSpanCount by DelegatedPreference(
        sharedPreferences,
        PREF_KEY_CURRENT_SPAN_COUNT,
        DEFAULT_SPAN_COUNT
    )
        private set

    private val _currentSpanCountChanged = MutableLiveData<Int>()
    val currentSpanCountChanged: LiveData<Int> = _currentSpanCountChanged

    private var currentSearchId = MutableLiveData<Long?>(null)

    val currentSearchUsers = currentSearchId.switchMap {
        getUsersUseCase(it).map {
            isLoading.value = false
            it
        }
    }

    private val _openUserEvent = MutableLiveData<Event<Long>>()
    val openUserEvent: LiveData<Event<Long>> = _openUserEvent

    val isLoading = MutableLiveData(true)

    fun onCurrentSearchIdObtained(searchId: Long) {
        currentSearchId.value = searchId
    }

    fun openUser(userId: Long) {
        _openUserEvent.value = Event(userId)
    }

    fun onItemChangeViewClicked() {
        currentSpanCount =
            if (currentSpanCount.dec() == 0) MAX_SPAN_COUNT else currentSpanCount.dec()
        _currentSpanCountChanged.value = currentSpanCount
    }
}