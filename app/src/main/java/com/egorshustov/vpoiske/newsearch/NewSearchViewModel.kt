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
import com.egorshustov.vpoiske.util.*
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

    private val _currentAgeFrom = MutableLiveData(DEFAULT_AGE_FROM)
    val currentAgeFrom: LiveData<Int?> = _currentAgeFrom

    private val _currentAgeTo = MutableLiveData(DEFAULT_AGE_TO)
    val currentAgeTo: LiveData<Int?> = _currentAgeTo

    private val _resetAgeToCommand = MutableLiveData<Event<Unit>>()
    val resetAgeToCommand: LiveData<Event<Unit>> = _resetAgeToCommand

    private val _resetAgeFromCommand = MutableLiveData<Event<Unit>>()
    val resetAgeFromCommand: LiveData<Event<Unit>> = _resetAgeFromCommand

    private val _currentRelation = MutableLiveData(Relation.NOT_DEFINED)
    val currentRelation: LiveData<Relation> = _currentRelation

    private val _usersCount = MutableLiveData(DEFAULT_USERS_COUNT)
    val usersCount: LiveData<Int> = _usersCount

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
                    is Result.Error -> showSnackBarMessage(citiesResponse.getString())
                }
            } else {
                _cities.value = emptyList()
            }
        }
    }

    fun onCitySelected(selectedCity: City) {
        _currentCity.value = selectedCity
    }

    fun onAgeFromSelected(ageFrom: Int?) {
        _currentAgeFrom.value = ageFrom
        currentAgeTo.value?.let {
            if (ageFrom != null && it < ageFrom) {
                _currentAgeTo.value = ageFrom
                _resetAgeToCommand.value = Event(Unit)
            }
        }
    }

    fun onAgeToSelected(ageTo: Int?) {
        _currentAgeTo.value = ageTo
        currentAgeFrom.value?.let {
            if (ageTo != null && it > ageTo) {
                _currentAgeFrom.value = ageTo
                _resetAgeFromCommand.value = Event(Unit)
            }
        }
    }

    fun onRelationSelected(relation: Relation) {
        _currentRelation.value = relation
    }

    fun onUsersCountChanged(usersCount: Int) {
        _usersCount.value = usersCount
    }

    private fun showSnackBarMessage(message: String) {
        _snackBarMessage.value = Event(message)
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}