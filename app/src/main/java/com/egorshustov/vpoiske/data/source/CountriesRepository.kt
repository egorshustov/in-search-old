package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.source.local.CountriesLocalDataSource
import com.egorshustov.vpoiske.data.source.remote.CountriesRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_COUNTRIES_COUNT
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountriesRepository @Inject constructor(
    private val countriesLocalDataSource: CountriesLocalDataSource,
    private val countriesRemoteDataSource: CountriesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getLiveCountries(): LiveData<List<Country>> = countriesLocalDataSource.getCountries()

    suspend fun getCountries(
        needAll: Boolean = false,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_GET_COUNTRIES_COUNT
    ) = withContext(ioDispatcher) {
        when (val getCountriesResult =
            countriesRemoteDataSource.getCountries(needAll, apiVersion, accessToken, count)) {
            is Result.Success -> {
                countriesLocalDataSource.saveCountries(getCountriesResult.data.map { it.toEntity() })
            }
            is Result.Error -> {
                Timber.e(getCountriesResult.exception)
                FirebaseCrashlytics.getInstance().recordException(getCountriesResult.exception)
            }
        }
    }
}