package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.CustomException
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.fakeSearchUsersInnerResponse
import com.egorshustov.vpoiske.util.fakeUserResponse
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject

class FakeUsersRepository @Inject constructor() : UsersRepository {

    private var usersMap: LinkedHashMap<Long, User> = LinkedHashMap()

    private val liveUsers = MutableLiveData<List<User>>()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun getUsers(): LiveData<List<User>> = liveUsers

    override suspend fun deleteUsersFromSearch(searchId: Long) {
        usersMap.remove(searchId)
        liveUsers.value = usersMap.values.toList()
    }

    override suspend fun saveUser(user: User): Long {
        val prevUser = usersMap.put(user.id, user)
        return if (prevUser == null) {
            liveUsers.value = usersMap.values.toList()
            1
        } else {
            0
        }
    }

    override suspend fun saveUsers(userList: List<User>): List<Long> {
        usersMap.putAll(userList.map { Pair(it.id, it) })
        liveUsers.value = usersMap.values.toList()
        return emptyList()
    }

    override suspend fun searchUsers(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        birthDay: Int,
        birthMonth: Int,
        fields: String,
        relation: Int?,
        sex: Int,
        hasPhoto: Int,
        apiVersion: String,
        accessToken: String,
        count: Int,
        sortType: Int
    ): Result<SearchUsersInnerResponse> = if (shouldReturnError) {
        Result.Error(CustomException(message = TEST_ERROR))
    } else {
        delay((100..5000).random().toLong())
        Result.Success(fakeSearchUsersInnerResponse)
    }

    override suspend fun getUser(
        userId: Long,
        fields: String,
        apiVersion: String,
        accessToken: String
    ): Result<UserResponse> = if (shouldReturnError) {
        Result.Error(CustomException(message = TEST_ERROR))
    } else {
        delay((100..5000).random().toLong())
        Result.Success(fakeUserResponse)
    }

    companion object {
        const val TEST_ERROR = "Test error text"
    }
}