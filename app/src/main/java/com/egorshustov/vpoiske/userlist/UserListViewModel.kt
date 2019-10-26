package com.egorshustov.vpoiske.userlist

import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.UsersRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel<UserListState>(UserListState()) {
    init {
        onSearchClick()
    }

    override fun onCleared() {
        val jj = 0
        super.onCleared()
    }

    fun onSearchClick() {
        viewModelScope.launch {
            val searchUsersResult = usersRepository.searchUsers(2, 18, 26, 14, 3)
            val getUserResult = usersRepository.getUser(1)
        }
    }
}