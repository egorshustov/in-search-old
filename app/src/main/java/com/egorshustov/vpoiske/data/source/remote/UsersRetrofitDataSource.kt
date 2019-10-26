package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUserResponse
import com.egorshustov.vpoiske.util.Sex
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRemoteDataSource {

    override suspend fun searchUsers(
        cityId: Int,
        ageFrom: Int,
        ageTo: Int,
        birthDay: Int,
        birthMonth: Int,
        hasPhoto: Int,
        fields: String,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<SearchUserResponse>> = withContext(ioDispatcher) {
        try {
            val response = retrofitVkApi.searchUsers(
                cityId,
                Sex.FEMALE.value,
                ageFrom,
                ageTo,
                birthDay,
                birthMonth,
                hasPhoto,
                fields,
                apiVersion,
                accessToken,
                count
            )
            if (response.isSuccessful) {
                response.body()?.response?.searchUserResponseList?.let {
                    return@withContext Result.Success(it)
                }
                return@withContext Result.Error(
                    Exception("searchUserResponseList is not found")
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
        userId: Int,
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