package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountriesRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher
) : CountriesRemoteDataSource {

    override suspend fun getCountries(
        needAll: Boolean,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<CountryResponse>> = withContext(ioDispatcher) {
        try {
            val response = retrofitVkApi.getCountries(
                if (needAll) 1 else 0,
                apiVersion,
                accessToken,
                count
            )
            val countryResponseList = response.body()?.response?.countryResponseList
            return@withContext if (response.isSuccessful && !countryResponseList.isNullOrEmpty()) {
                Result.Success(countryResponseList)
            } else {
                val vkErrorResponse = response.body()?.error
                var errorText = vkErrorResponse?.errorMessage
                if (errorText.isNullOrBlank()) errorText = GET_COUNTRIES_ERROR
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
        private const val GET_COUNTRIES_ERROR = "Не удалось получить список стран"
    }
}