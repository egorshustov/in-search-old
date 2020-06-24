package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.local.UsersLocalDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.UsersRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getUsers(): LiveData<List<User>> = usersLocalDataSource.getUsers()

    suspend fun saveUser(user: User): Long =
        withContext(ioDispatcher) { usersLocalDataSource.saveUser(user) }

    suspend fun saveUsers(userList: List<User>): List<Long> =
        withContext(ioDispatcher) { usersLocalDataSource.saveUsers(userList) }

    suspend fun searchUsers(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        birthDay: Int,
        birthMonth: Int,
        fields: String,
        relation: Int? = Relation.NOT_DEFINED.value,
        sex: Int = Sex.FEMALE.value,
        hasPhoto: Int = HasPhoto.NECESSARY.value,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_SEARCH_USERS_COUNT,
        sortType: Int = SortType.BY_REGISTRATION_DATE.value
    ): Result<SearchUsersInnerResponse> = usersRemoteDataSource.searchUsers(
        countryId,
        cityId,
        ageFrom,
        ageTo,
        birthDay,
        birthMonth,
        fields,
        relation,
        sex,
        hasPhoto,
        apiVersion,
        accessToken,
        count,
        sortType
    )

    suspend fun getUser(
        userId: Long,
        fields: String = DEFAULT_GET_USER_FIELDS,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken
    ): Result<UserResponse> = usersRemoteDataSource.getUser(
        userId,
        fields,
        apiVersion,
        accessToken
    )
}