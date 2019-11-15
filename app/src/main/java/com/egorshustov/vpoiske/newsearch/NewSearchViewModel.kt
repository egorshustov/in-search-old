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

    private val _currentFoundedUsersLimit = MutableLiveData(DEFAULT_FOUNDED_USERS_LIMIT)
    val currentFoundedUsersLimit: LiveData<Int> = _currentFoundedUsersLimit

    private var defaultFriendsMaxCount = 250
    private val _currentFriendsMaxCount = MutableLiveData<Int?>(defaultFriendsMaxCount)
    val currentFriendsMaxCount: LiveData<Int?> = _currentFriendsMaxCount

    var defaultFriendsMinCount = 50
        private set
    private val _currentFriendsMinCount = MutableLiveData<Int?>(defaultFriendsMinCount)
    val currentFriendsMinCount: LiveData<Int?> = _currentFriendsMinCount

    private val _currentFollowersMaxCount = MutableLiveData(DEFAULT_FOLLOWERS_MAX_COUNT)
    val currentFollowersMaxCount: LiveData<Int> = _currentFollowersMaxCount

    private val _currentFollowersMinCount = MutableLiveData(DEFAULT_FOLLOWERS_MIN_COUNT)
    val currentFollowersMinCount: LiveData<Int> = _currentFollowersMinCount

    private val _currentDaysInterval = MutableLiveData(DEFAULT_DAYS_INTERVAL)
    val currentDaysInterval: LiveData<Int> = _currentDaysInterval

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

    fun onFoundedUsersLimitChanged(usersCount: Int) {
        _currentFoundedUsersLimit.value = usersCount
    }

    fun onFriendsMaxCountChanged(friendsMaxCount: Int) {
        _currentFriendsMaxCount.value = friendsMaxCount
        defaultFriendsMaxCount = friendsMaxCount
    }

    fun onFollowersMaxCountChanged(followersMaxCount: Int) {
        _currentFollowersMaxCount.value = followersMaxCount
    }

    fun onSetFriendsLimitsChanged(isChecked: Boolean) {
        _currentFriendsMinCount.value = if (isChecked) defaultFriendsMinCount else null
        _currentFriendsMaxCount.value = if (isChecked) defaultFriendsMaxCount else null
    }

    fun onDaysIntervalChanged(daysInterval: Int) {
        _currentDaysInterval.value = daysInterval
    }

    private fun showSnackBarMessage(message: String) {
        Timber.d("showSnackBarMessage: $message")
        _snackBarMessage.value = Event(message)
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}