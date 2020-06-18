package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.local.CountriesDao
import com.egorshustov.vpoiske.data.source.remote.CountriesRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
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
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCountries(
        needAll: Boolean = false,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_GET_COUNTRIES_COUNT
    ) =
        withContext(ioDispatcher) {
            val getCountriesResult =
                countriesRemoteDataSource.getCountries(needAll, apiVersion, accessToken, count)
            Timber.d(getCountriesResult.toString())
            if (getCountriesResult is Result.Success) {
                countriesDao.insertCountries(getCountriesResult.data.map { it.toEntity() })
            }
        }

    fun getLiveCountries() = countriesDao.getLiveCountries()
}