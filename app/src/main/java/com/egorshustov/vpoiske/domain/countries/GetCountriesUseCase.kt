package com.egorshustov.vpoiske.domain.countries

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.source.CountriesRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val countriesRepository: CountriesRepository) {

    operator fun invoke(): LiveData<List<Country>> = countriesRepository.getCountries()
}