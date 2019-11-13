package com.egorshustov.vpoiske.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.MILLIS_IN_SECOND
import com.egorshustov.vpoiske.util.SECONDS_IN_DAY
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

    @Volatile
    private var foundUsersCount: Int = 0

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
        foundedUsersLimit: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int,
        daysInterval: Int
    ) = viewModelScope.launch {
        val searchStartUnixSeconds = (System.currentTimeMillis() / MILLIS_IN_SECOND).toInt()
        while (true) {
            val randomDay = (1..31).random()
            val randomMonth = (1..12).random()
            when (val searchUsersResult = usersRepository.searchUsers(
                countryId,
                cityId,
                ageFrom,
                ageTo,
                randomDay,
                randomMonth,
                relation,
                sex
            )) {
                is Result.Success -> {
                    val searchUsersInnerResponse =
                        searchUsersResult.data as SearchUsersInnerResponse
                    val count = searchUsersInnerResponse.count
                    //todo if count > 1000 split request
                    val searchUserResponseList = searchUsersInnerResponse.searchUserResponseList
                    if (!searchUserResponseList.isNullOrEmpty()) {
                        handleSearchUsersSuccess(
                            searchUserResponseList,
                            searchStartUnixSeconds,
                            withPhoneOnly,
                            foundedUsersLimit,
                            friendsMinCount,
                            friendsMaxCount,
                            followersMinCount,
                            followersMaxCount,
                            daysInterval
                        )
                    }

                }
                is Result.Error -> handleError(searchUsersResult.exception)
            }
        }
    }

    private suspend fun handleSearchUsersSuccess(
        searchUserResponseList: List<SearchUserResponse>,
        searchStartUnixSeconds: Int,
        withPhoneOnly: Boolean,
        foundedUsersLimit: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int,
        daysInterval: Int
    ) {
        val filteredList = searchUserResponseList.filter {
            val isNotClosed = it.isClosed == false
            val isFollowersCountAcceptable = (it.followersCount ?: 0) >= followersMinCount
                    && (it.followersCount ?: 0) <= followersMaxCount
            val isInDaysInterval = searchStartUnixSeconds - (it.lastSeen?.timeUnixSeconds
                ?: 0) < daysInterval * SECONDS_IN_DAY
            val phoneCheckPassed = if (withPhoneOnly) it.hasCorrectPhone() else true
            isNotClosed && isFollowersCountAcceptable && isInDaysInterval && phoneCheckPassed
        }
        //todo for each check if in DB
        //todo if not in DB and friends max limit is set, getUsersRequest and check friends count
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