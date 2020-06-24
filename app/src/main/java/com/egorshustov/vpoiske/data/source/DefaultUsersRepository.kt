package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.local.UsersLocalDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.UsersRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUsersRepository @Inject constructor(
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : UsersRepository {

    override fun getUsers(): LiveData<List<User>> = usersLocalDataSource.getUsers()

    override suspend fun saveUser(user: User): Long =
        withContext(ioDispatcher) { usersLocalDataSource.saveUser(user) }

    override suspend fun saveUsers(userList: List<User>): List<Long> =
        withContext(ioDispatcher) { usersLocalDataSource.saveUsers(userList) }

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

    override suspend fun getUser(
        userId: Long,
        fields: String,
        apiVersion: String,
        accessToken: String
    ): Result<UserResponse> = usersRemoteDataSource.getUser(
        userId,
        fields,
        apiVersion,
        accessToken
    )
}