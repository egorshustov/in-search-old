package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.Country
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountriesRoomDataSource @Inject constructor(
    private val countriesDao: CountriesDao,
    private val ioDispatcher: CoroutineDispatcher
) : CountriesLocalDataSource {

    override fun getCountries(): LiveData<List<Country>> = countriesDao.getLiveCountries()

    override suspend fun saveCountries(countryList: List<Country>) = withContext(ioDispatcher) {
        countriesDao.insertCountries(countryList)
    }
}