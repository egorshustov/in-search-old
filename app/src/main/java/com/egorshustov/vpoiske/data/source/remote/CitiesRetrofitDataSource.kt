package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CitiesRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CitiesRemoteDataSource {
    override suspend fun getCities(
        countryId: Int,
        needAll: Boolean,
        searchQuery: String,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<CityResponse>> =
        withContext(ioDispatcher) {
            try {
                val response = retrofitVkApi.getCities(
                    countryId,
                    if (needAll) 1 else 0,
                    searchQuery,
                    apiVersion,
                    accessToken,
                    count
                )
                if (response.isSuccessful) {
                    response.body()?.response?.cityResponseList?.let {
                        return@withContext Result.Success(it)
                    }
                    return@withContext Result.Error(
                        Exception("cityResponseList is not found")
                    )
                } else {
                    return@withContext Result.Error(
                        Exception("getCities response is not successful")
                    )
                }
            } catch (e: Exception) {
                return@withContext Result.Error(
                    Exception(e)
                )
            }
        }
}