package com.egorshustov.vpoiske.newsearch

import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.source.CountriesRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NewSearchViewModel @Inject constructor(
    countriesRepository: CountriesRepository
) : BaseViewModel<NewSearchState>(NewSearchState()) {
    val liveCountries = countriesRepository.getLiveCountries()

    init {
        viewModelScope.launch {
            countriesRepository.getCountries()
        }
    }
}