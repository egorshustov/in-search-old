package com.egorshustov.vpoiske.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.util.Event
import javax.inject.Inject

class SearchViewModel @Inject constructor(usersRepository: UsersRepository) : ViewModel() {

    private val users: LiveData<List<User>> = usersRepository.getLiveUsers()

    val currentSearchUsers: LiveData<List<User>> = Transformations.map(users) { users ->
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
        isLoading.value = false
        currentSearchId = searchId
    }
}