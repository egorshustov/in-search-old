package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUserResponse

interface UsersRemoteDataSource {

    suspend fun searchUsers(
        cityId: Int,
        ageFrom: Int,
        ageTo: Int,
        birthDay: Int,
        birthMonth: Int,
        hasPhoto: Int,
        fields: String,
        apiVersion: String,
        accessToken: String,
        count: Int,
        sortType: Int
    ): Result<List<SearchUserResponse>>

    suspend fun getUser(
        userId: Int,
        fields: String,
        apiVersion: String,
        accessToken: String
    ): Result<UserResponse>
}