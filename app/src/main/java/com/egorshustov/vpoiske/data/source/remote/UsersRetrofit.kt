package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.GetUserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersRetrofit {
    @GET("users.search")
    suspend fun searchUsers(
        @Query("count") count: Int,
        @Query("city") cityId: Int,
        @Query("sex") sex: Int,
        @Query("age_from") ageFrom: Int,
        @Query("age_to") ageTo: Int,
        @Query("birth_day") birthDay: Int,
        @Query("birth_month") birthMonth: Int,
        @Query("has_photo") hasPhoto: Int,
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<SearchUsersResponse>

    @GET("users.get")
    suspend fun getUser(
        @Query("user_ids") userId: Int,
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<GetUserResponse>
}