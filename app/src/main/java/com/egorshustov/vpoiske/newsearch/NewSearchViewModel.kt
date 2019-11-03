package com.egorshustov.vpoiske.newsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.base.BaseViewModel
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.source.CitiesRepository
import com.egorshustov.vpoiske.data.source.CountriesRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.util.DEFAULT_CITY
import com.egorshustov.vpoiske.util.DEFAULT_COUNTRY
import com.egorshustov.vpoiske.util.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NewSearchViewModel @Inject constructor(
    countriesRepository: CountriesRepository,
    private val citiesRepository: CitiesRepository
) : BaseViewModel<NewSearchState>(NewSearchState()) {
    val countries = countriesRepository.getLiveCountries()
    val cities = MutableLiveData<List<City>>()

    init {
        Timber.d("%s init", toString())
        viewModelScope.launch {
            countriesRepository.getCountries()
        }
    }

    fun onCountrySelected(selectedCountry: Country) = viewModelScope.launch {
        if (state.country.value != selectedCountry) {
            state.country.value = selectedCountry
            state.city.value = DEFAULT_CITY
            if (selectedCountry != DEFAULT_COUNTRY) {
                when (val citiesResponse = citiesRepository.getCities(selectedCountry.id)) {
                    is Result.Success -> cities.value = citiesResponse.data.map { it.toEntity() }
                    is Result.Error -> state.snackBarMessage.value =
                        Event(citiesResponse.getString())
                }
            } else {
                cities.value = emptyList()
            }
        }
    }

    fun onCitySelected(selectedCity: City) {
        state.city.value = selectedCity
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}