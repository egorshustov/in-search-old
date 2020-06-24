package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_CITIES_COUNT

interface CitiesRepository {

    suspend fun getCities(
        countryId: Int,
        needAll: Boolean = false,
        searchQuery: String = "",
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_GET_CITIES_COUNT
    ): Result<List<CityResponse>>
}