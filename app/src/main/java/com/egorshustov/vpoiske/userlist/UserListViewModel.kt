package com.egorshustov.vpoiske.userlist

import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.UsersRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel<UserListState>(UserListState()) {
    init {
        Timber.d("%s init", toString())
        onSearchClick()
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }

    fun onSearchClick() {
        viewModelScope.launch {
            val searchUsersResult = usersRepository.searchUsers(1, 2, 18, 26, 14, 3)
            val getUserResult = usersRepository.getUser(1)
        }
    }
}