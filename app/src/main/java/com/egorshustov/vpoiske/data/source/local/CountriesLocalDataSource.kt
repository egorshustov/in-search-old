package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.Country

interface CountriesLocalDataSource {

    fun getCountries(): LiveData<List<Country>>

    suspend fun saveCountries(countryList: List<Country>)
}