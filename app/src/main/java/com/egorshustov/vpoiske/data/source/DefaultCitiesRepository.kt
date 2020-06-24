package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.remote.CitiesRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultCitiesRepository @Inject constructor(
    private val citiesRemoteDataSource: CitiesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : CitiesRepository {

    override suspend fun getCities(
        countryId: Int,
        needAll: Boolean,
        searchQuery: String,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<CityResponse>> = withContext(ioDispatcher) {
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