package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse

interface CitiesRemoteDataSource {

    suspend fun getCities(
        countryId: Int,
        needAll: Boolean,
        searchQuery: String,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<CityResponse>>
}