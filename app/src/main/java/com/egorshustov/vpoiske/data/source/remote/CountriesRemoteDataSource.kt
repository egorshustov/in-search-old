package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse

interface CountriesRemoteDataSource {

    suspend fun getCountries(
        needAll: Boolean,
        apiVersion: String,
        accessToken: String,
        count: Int
    ): Result<List<CountryResponse>>
}