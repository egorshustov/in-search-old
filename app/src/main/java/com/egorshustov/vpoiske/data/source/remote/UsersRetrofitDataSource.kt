package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher
) : UsersRemoteDataSource {

    override suspend fun searchUsers(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        birthDay: Int,
        birthMonth: Int,
        fields: String,
        homeTown: String?,
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
                homeTown,
                relation,
                sex,
                hasPhoto,
                apiVersion,
                accessToken,
                count,
                sortType
            )
            val searchUsersInnerResponse = response.body()?.response
            return@withContext if (response.isSuccessful && searchUsersInnerResponse != null) {
                Result.Success(searchUsersInnerResponse)
            } else {
                val vkErrorResponse = response.body()?.error
                var errorText = vkErrorResponse?.errorMessage
                if (errorText.isNullOrBlank()) errorText = SEARCH_USERS_ERROR
                Result.Error(
                    CustomException(
                        message = errorText,
                        vkErrorCode = vkErrorResponse?.errorCode
                    )
                )
            }
        } catch (t: Throwable) {
            return@withContext Result.Error(CustomException(cause = t))
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
            val userResponse = response.body()?.userResponseList?.firstOrNull()
            return@withContext if (response.isSuccessful && userResponse != null) {
                Result.Success(userResponse)
            } else {
                val vkErrorResponse = response.body()?.error
                var errorText = vkErrorResponse?.errorMessage
                if (errorText.isNullOrBlank()) errorText = GET_USER_ERROR
                Result.Error(
                    CustomException(
                        message = errorText,
                        vkErrorCode = vkErrorResponse?.errorCode
                    )
                )
            }
        } catch (t: Throwable) {
            return@withContext Result.Error(CustomException(cause = t))
        }
    }

    companion object {
        private const val SEARCH_USERS_ERROR = "Не удалось выполнить поиск пользователей"
        private const val GET_USER_ERROR = "Не удалось получить информацию о пользователе"
    }
}