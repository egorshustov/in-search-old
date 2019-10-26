package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.local.CountriesDao
import com.egorshustov.vpoiske.data.source.remote.CountriesRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.util.DEFAULT_GET_COUNTRIES_COUNT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesRepository @Inject constructor(
    private val countriesDao: CountriesDao,
    private val countriesRemoteDataSource: CountriesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getCountries(needAll: Boolean = false, count: Int = DEFAULT_GET_COUNTRIES_COUNT) =
        withContext(ioDispatcher) {
            val getCountriesResult = countriesRemoteDataSource.getCountries(needAll, count)
            Timber.d(getCountriesResult.toString())
            if (getCountriesResult is Result.Success) {
                countriesDao.addCountries(getCountriesResult.data.map { it.toEntity() })
            }
        }

    fun getLiveCountries() = countriesDao.getLiveCountries()
}