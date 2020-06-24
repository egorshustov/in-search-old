package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CitiesRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher
) : CitiesRemoteDataSource {

    override suspend fun getCities(
        countryId: Int,
        needAll: Boolean,
        searchQuery: String,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<CityResponse>> = withContext(ioDispatcher) {
        try {
            val response = retrofitVkApi.getCities(
                countryId,
                if (needAll) 1 else 0,
                searchQuery,
                apiVersion,
                accessToken,
                count
            )
            val cityResponseList = response.body()?.response?.cityResponseList
            return@withContext if (response.isSuccessful && !cityResponseList.isNullOrEmpty()) {
                Result.Success(cityResponseList)
            } else {
                val vkErrorResponse = response.body()?.error
                var errorText = vkErrorResponse?.errorMessage
                if (errorText.isNullOrBlank()) errorText = GET_CITIES_ERROR
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
        private const val GET_CITIES_ERROR = "Не удалось получить список городов"
    }
}