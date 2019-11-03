package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.local.CitiesDao
import com.egorshustov.vpoiske.data.source.remote.CitiesRemoteDataSource
import com.egorshustov.vpoiske.util.ACCESS_TOKEN
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_CITIES_COUNT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(
    private val citiesDao: CitiesDao,
    private val citiesRemoteDataSource: CitiesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getCities(
        countryId: Int,
        needAll: Boolean = false,
        searchQuery: String = "",
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = ACCESS_TOKEN,
        count: Int = DEFAULT_GET_CITIES_COUNT
    ) =
        withContext(ioDispatcher) {
                citiesRemoteDataSource.getCities(
                    countryId,
                    needAll,
                    searchQuery,
                    apiVersion,
                    accessToken,
                    count
                )
        }
}