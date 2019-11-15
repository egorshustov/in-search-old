package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse

interface UsersRemoteDataSource {

    suspend fun searchUsers(
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
    ): Result<SearchUsersInnerResponse>

    suspend fun getUser(
        userId: Int,
        fields: String,
        apiVersion: String,
        accessToken: String
    ): Result<UserResponse>
}