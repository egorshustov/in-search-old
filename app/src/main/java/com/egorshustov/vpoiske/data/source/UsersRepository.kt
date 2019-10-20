package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.local.UsersDao
import com.egorshustov.vpoiske.data.source.remote.UsersRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersDao: UsersDao,
    private val usersRemoteDataSource: UsersRemoteDataSource
) {
    suspend fun searchUsers(cityId: Int, ageFrom: Int, ageTo: Int, birthDay: Int, birthMonth: Int) =
        usersRemoteDataSource.searchUsers(
            cityId,
            ageFrom,
            ageTo,
            birthDay,
            birthMonth
        )

    suspend fun getUser(userId: Int) = usersRemoteDataSource.getUser(userId)
}