package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.Item
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRetrofitDataSource @Inject constructor(
    private val usersRetrofit: UsersRetrofit,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRemoteDataSource {

    override suspend fun searchUsers(
        cityId: Int,
        ageFrom: Int,
        ageTo: Int,
        birthDay: Int,
        birthMonth: Int
    ): Result<List<Item>> = withContext(ioDispatcher) {
        try {
            val response = usersRetrofit.searchUsers(
                DEFAULT_SEARCH_USERS_COUNT,
                cityId,
                Sex.FEMALE.value,
                ageFrom,
                ageTo,
                birthDay,
                birthMonth,
                HasPhoto.NECESSARY.value,
                DEFAULT_SEARCH_USERS_FIELDS,
                DEFAULT_API_VERSION,
                ACCESS_TOKEN
            )
            if (response.isSuccessful) {
                response.body()?.response?.items?.let {
                    return@withContext Result.Success(it)
                }
                return@withContext Result.Error(
                    Exception("items are not found")
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
        userId: Int
    ): Result<UserResponse> = withContext(ioDispatcher) {
        try {
            val response = usersRetrofit.getUser(
                userId,
                DEFAULT_GET_USER_FIELDS,
                DEFAULT_API_VERSION,
                ACCESS_TOKEN
            )
            if (response.isSuccessful) {
                response.body()?.userResponseList?.firstOrNull()?.let {
                    return@withContext Result.Success(it)
                }
                return@withContext Result.Error(
                    Exception("userResponse is not found")
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