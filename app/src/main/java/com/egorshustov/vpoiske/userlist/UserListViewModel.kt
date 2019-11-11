package com.egorshustov.vpoiske.userlist

import com.egorshustov.vpoiske.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class UserListViewModel @Inject constructor(
) : BaseViewModel<UserListState>(UserListState()) {
    init {
        Timber.d("%s init", toString())
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}