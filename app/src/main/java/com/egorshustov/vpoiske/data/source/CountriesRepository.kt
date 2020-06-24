package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_COUNTRIES_COUNT

interface CountriesRepository {

    fun getLiveCountries(): LiveData<List<Country>>

    suspend fun getCountries(
        needAll: Boolean = false,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_GET_COUNTRIES_COUNT
    )
}