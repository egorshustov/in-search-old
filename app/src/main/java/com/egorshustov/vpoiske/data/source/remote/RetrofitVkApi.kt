package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcities.GetCitiesResponse
import com.egorshustov.vpoiske.data.source.remote.getcountries.GetCountriesResponse
import com.egorshustov.vpoiske.data.source.remote.getuser.GetUserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitVkApi {
    @GET("users.search")
    suspend fun searchUsers(
        @Query("country") countryId: Int,
        @Query("city") cityId: Int,
        @Query("age_from") ageFrom: Int?,
        @Query("age_to") ageTo: Int?,
        @Query("birth_day") birthDay: Int,
        @Query("birth_month") birthMonth: Int,
        @Query("fields") fields: String,
        @Query("status") relation: Int?,
        @Query("sex") sex: Int,
        @Query("has_photo") hasPhoto: Int,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String,
        @Query("count") count: Int,
        @Query("sort") sortType: Int
    ): Response<SearchUsersResponse>

    @GET("users.get")
    suspend fun getUser(
        @Query("user_ids") userId: Long,
        @Query("fields") fields: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String
    ): Response<GetUserResponse>

    @GET("database.getCountries")
    suspend fun getCountries(
        @Query("need_all") needAll: Int,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String,
        @Query("count") count: Int
    ): Response<GetCountriesResponse>

    @GET("database.getCities")
    suspend fun getCities(
        @Query("country_id") countryId: Int,
        @Query("need_all") needAll: Int,
        @Query("q") searchQuery: String,
        @Query("v") apiVersion: String,
        @Query("access_token") accessToken: String,
        @Query("count") count: Int
    ): Response<GetCitiesResponse>
}