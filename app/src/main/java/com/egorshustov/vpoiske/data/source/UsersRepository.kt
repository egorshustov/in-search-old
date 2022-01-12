package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.*

interface UsersRepository {

    fun getUsers(): LiveData<List<User>>

    suspend fun deleteUsersFromSearch(searchId: Long)

    suspend fun saveUser(user: User): Long

    suspend fun saveUsers(userList: List<User>): List<Long>

    suspend fun searchUsers(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        birthDay: Int,
        birthMonth: Int,
        fields: String,
        homeTown: String? = null,
        relation: Int? = Relation.NOT_DEFINED.value,
        sex: Int = Sex.FEMALE.value,
        hasPhoto: Int = HasPhoto.NECESSARY.value,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_SEARCH_USERS_COUNT,
        sortType: Int = SortType.BY_REGISTRATION_DATE.value
    ): Result<SearchUsersInnerResponse>

    suspend fun getUser(
        userId: Long,
        fields: String = DEFAULT_GET_USER_FIELDS,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken
    ): Result<UserResponse>
}