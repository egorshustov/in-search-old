package com.egorshustov.vpoiske.domain.countries

import com.egorshustov.vpoiske.data.source.CountriesRepository
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_COUNTRIES_COUNT
import javax.inject.Inject

class RequestCountriesUseCase @Inject constructor(private val countriesRepository: CountriesRepository) {

    suspend operator fun invoke(
        needAll: Boolean = false,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_GET_COUNTRIES_COUNT
    ) = countriesRepository.requestCountries(needAll, apiVersion, accessToken, count)
}