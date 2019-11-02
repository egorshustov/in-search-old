package com.egorshustov.vpoiske.newsearch

import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.CitiesRepository
import com.egorshustov.vpoiske.data.source.CountriesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewSearchViewModel @Inject constructor(
    countriesRepository: CountriesRepository,
    citiesRepository: CitiesRepository
) : BaseViewModel<NewSearchState>(NewSearchState()) {
    val liveCountries = countriesRepository.getLiveCountries()
    val liveCities = citiesRepository.getLiveCities()


    init {
        viewModelScope.launch {
            countriesRepository.getCountries()
            citiesRepository.getCities(1)
        }
    }
}