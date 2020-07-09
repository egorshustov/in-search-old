package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.analytics.VPoiskeAnalytics
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.source.local.CountriesLocalDataSource
import com.egorshustov.vpoiske.data.source.remote.CountriesRemoteDataSource
import com.egorshustov.vpoiske.data.source.remote.Result
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultCountriesRepository @Inject constructor(
    private val countriesLocalDataSource: CountriesLocalDataSource,
    private val countriesRemoteDataSource: CountriesRemoteDataSource,
    private val vPoiskeAnalytics: VPoiskeAnalytics,
    private val ioDispatcher: CoroutineDispatcher
) : CountriesRepository {

    override fun getCountries(): LiveData<List<Country>> =
        countriesLocalDataSource.getCountries()

    override suspend fun requestCountries(
        needAll: Boolean,
        apiVersion: String,
        accessToken: String,
        count: Int
    ) = withContext(ioDispatcher) {
        when (val getCountriesResult =
            countriesRemoteDataSource.getCountries(needAll, apiVersion, accessToken, count)) {
            is Result.Success -> {
                countriesLocalDataSource.saveCountries(getCountriesResult.data.map { it.toEntity() })
            }
            is Result.Error -> {
                Timber.e(getCountriesResult.exception)
                FirebaseCrashlytics.getInstance().recordException(getCountriesResult.exception)
                vPoiskeAnalytics.errorOccurred(
                    getCountriesResult.getString(),
                    getCountriesResult.exception.cause?.toString(),
                    getCountriesResult.exception.vkErrorCode
                )
            }
        }
    }
}