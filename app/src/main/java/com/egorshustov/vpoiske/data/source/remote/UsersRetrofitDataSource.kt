package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRemoteDataSource {

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
    ): Result<SearchUsersInnerResponse> = withContext(ioDispatcher) {
        try {
            val response = retrofitVkApi.searchUsers(
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
            if (response.isSuccessful) {
                response.body()?.response?.let {
                    return@withContext Result.Success(it)
                }
                return@withContext Result.Error(
                    Exception("SearchUsersInnerResponse is not found")
                )
            } else {
                return@withContext Result.Error(
                    Exception("searchUsers response is not successful")
                )
            }
        } catch (e: Exception) {
            return@withContext Result.Error(
                Exception(e)
            )
        }
    }

    override suspend fun getUser(
        userId: Long,
        fields: String,
        apiVersion: String,
        accessToken: String
    ): Result<UserResponse> = withContext(ioDispatcher) {
        try {
            val response = retrofitVkApi.getUser(
                userId,
                fields,
                apiVersion,
                accessToken
            )
            if (response.isSuccessful) {
                response.body()?.userResponseList?.firstOrNull()?.let {
                    return@withContext Result.Success(it)
                }
                return@withContext Result.Error(
                    Exception("userResponseList is not found")
                )
            } else {
                return@withContext Result.Error(
                    Exception("getUser response is not successful")
                )
            }
        } catch (e: Exception) {
            return@withContext Result.Error(
                Exception(e)
            )
        }
    }
}