package com.egorshustov.vpoiske.search

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.util.*

class SearchViewModel @ViewModelInject constructor(
    usersRepository: UsersRepository,
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

    private val users: LiveData<List<User>> = usersRepository.getLiveUsers()

    val currentSearchUsers: LiveData<List<User>> = Transformations.map(users) { users ->
        isLoading.value = false
        users.filter { it.searchId == currentSearchId }
    }

    private var currentSearchId: Long? = null

    private val _openUserEvent = MutableLiveData<Event<Long>>()
    val openUserEvent: LiveData<Event<Long>> = _openUserEvent

    val isLoading = MutableLiveData<Boolean>(true)

    fun openUser(userId: Long) {
        _openUserEvent.value = Event(userId)
    }

    fun onCurrentSearchIdObtained(searchId: Long) {
        currentSearchId = searchId
    }

    fun onItemChangeViewClicked() {
        currentSpanCount =
            if (currentSpanCount.dec() == 0) MAX_SPAN_COUNT else currentSpanCount.dec()
        _currentSpanCountChanged.value = currentSpanCount
    }
}