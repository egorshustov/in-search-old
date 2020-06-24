package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.remote.CitiesRemoteDataSource
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_CITIES_COUNT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(
    private val citiesRemoteDataSource: CitiesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCities(
        countryId: Int,
        needAll: Boolean = false,
        searchQuery: String = "",
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
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