package com.egorshustov.vpoiske.data.source.remote

import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse

interface CountriesRemoteDataSource {

    suspend fun getCountries(needAll: Boolean, count: Int): Result<List<CountryResponse>>
}