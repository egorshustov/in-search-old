package com.egorshustov.vpoiske.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel<MainState>(MainState()) {

    enum class SearchState {
        INACTIVE,
        IN_PROGRESS
    }

    val searchState = MutableLiveData<SearchState>(SearchState.INACTIVE)

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
        usersCount: Int,
        daysInterval: Int
    ) = viewModelScope.launch {
        when (val searchUsersResult = usersRepository.searchUsers(
            countryId,
            cityId,
            ageFrom,
            ageTo,
            12,
            2,
            relation,
            sex
        )) {
            is Result.Success -> handleSearchUsersSuccess(searchUsersResult.data)
            is Result.Error -> handleError(searchUsersResult.exception)
        }
    }

    private suspend fun handleSearchUsersSuccess(searchUsersInnerResponse: SearchUsersInnerResponse) {


        val getUserResult = usersRepository.getUser(1)
    }

    private fun handleError(exception: Exception) {
        Timber.d("searchUsers exception: $exception")
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}