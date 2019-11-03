package com.egorshustov.vpoiske.newsearch

import androidx.lifecycle.LiveData
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

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    private val _currentCountry = MutableLiveData(DEFAULT_COUNTRY)
    val currentCountry: LiveData<Country> = _currentCountry

    private val _currentCity = MutableLiveData(DEFAULT_CITY)
    val currentCity: LiveData<City> = _currentCity

    private val _snackBarMessage = MutableLiveData<Event<String>>()
    val snackBarMessage: LiveData<Event<String>> = _snackBarMessage

    init {
        Timber.d("%s init", toString())
        viewModelScope.launch {
            countriesRepository.getCountries()
        }
    }

    fun onCountrySelected(selectedCountry: Country) = viewModelScope.launch {
        if (currentCountry.value != selectedCountry) {
            _currentCountry.value = selectedCountry
            _currentCity.value = DEFAULT_CITY
            if (selectedCountry != DEFAULT_COUNTRY) {
                when (val citiesResponse = citiesRepository.getCities(selectedCountry.id)) {
                    is Result.Success -> _cities.value = citiesResponse.data.map { it.toEntity() }
                    is Result.Error -> _snackBarMessage.value =
                        Event(citiesResponse.getString())
                }
            } else {
                _cities.value = emptyList()
            }
        }
    }

    fun onCitySelected(selectedCity: City) {
        _currentCity.value = selectedCity
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}