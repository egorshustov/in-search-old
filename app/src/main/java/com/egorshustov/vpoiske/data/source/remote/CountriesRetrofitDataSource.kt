package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse
import com.egorshustov.vpoiske.util.ACCESS_TOKEN
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountriesRetrofitDataSource @Inject constructor(
    private val retrofitVkApi: RetrofitVkApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CountriesRemoteDataSource {
    override suspend fun getCountries(needAll: Boolean, count: Int): Result<List<CountryResponse>> =
        withContext(ioDispatcher) {
            try {
                val response = retrofitVkApi.getCountries(
                    if (needAll) 1 else 0,
                    count,
                    DEFAULT_API_VERSION,
                    ACCESS_TOKEN
                )
                if (response.isSuccessful) {
                    response.body()?.response?.countryResponseList?.let {
                        return@withContext Result.Success(it)
                    }
                    return@withContext Result.Error(
                        Exception("countryResponseList is not found")
                    )
                } else {
                    return@withContext Result.Error(
                        Exception("getCountries response is not successful")
                    )
                }
            } catch (e: Exception) {
                return@withContext Result.Error(
                    Exception(e)
                )
            }
        }
}