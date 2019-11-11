package com.egorshustov.vpoiske.main

import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.UsersRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel<MainState>(MainState()) {
    init {
        Timber.d("%s init", toString())
    }

    fun onSearchButtonClicked(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        relation: Int?,
        sex: Int,
        withPhoneOnly: Boolean,
        usersCount: Int
    ) {
        viewModelScope.launch {
            val searchUsersResult =
                usersRepository.searchUsers(countryId, cityId, ageFrom, ageTo, 14, 3, relation, sex)

            val getUserResult = usersRepository.getUser(1)
        }
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}