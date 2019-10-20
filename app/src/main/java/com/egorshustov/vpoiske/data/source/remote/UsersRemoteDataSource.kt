package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.Item

interface UsersRemoteDataSource {

    suspend fun searchUsers(
        cityId: Int,
        ageFrom: Int,
        ageTo: Int,
        birthDay: Int,
        birthMonth: Int
    ): Result<List<Item>>

    suspend fun getUser(userId: Int): Result<UserResponse>
}