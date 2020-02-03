package com.egorshustov.vpoiske.searchparams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.source.CitiesRepository
import com.egorshustov.vpoiske.data.source.CountriesRepository
import com.egorshustov.vpoiske.data.source.SearchesRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SearchParamsViewModel @Inject constructor(
    countriesRepository: CountriesRepository,
    private val citiesRepository: CitiesRepository,
    private val searchesRepository: SearchesRepository
) : ViewModel() {

    val countries: LiveData<List<Country>> = countriesRepository.getLiveCountries()

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    val currentCountry = MutableLiveData(Event(DEFAULT_COUNTRY_TITLE))

    val currentCity = MutableLiveData(DEFAULT_CITY_TITLE)

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

    private val _currentSex = MutableLiveData(Sex.FEMALE)
    val currentSex: LiveData<Sex> = _currentSex

    private val _currentWithPhoneOnly = MutableLiveData(false)
    val currentWithPhoneOnly: LiveData<Boolean> = _currentWithPhoneOnly

    private val _currentFoundUsersLimit = MutableLiveData(DEFAULT_FOUND_USERS_LIMIT)
    val currentFoundUsersLimit: LiveData<Int> = _currentFoundUsersLimit

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

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _newSearchId = MutableLiveData<Event<Long?>>(null)
    val newSearchId: LiveData<Event<Long?>> = _newSearchId

    init {
        Timber.d("%s init", toString())
        viewModelScope.launch {
            countriesRepository.getCountries()
        }
    }

    fun onCountrySelected(countryId: Int) {
        currentCity.value = DEFAULT_CITY_TITLE
        getCities(countryId)
    }

    private fun getCities(countryId: Int) = viewModelScope.launch {
        if (countryId != DEFAULT_COUNTRY_TITLE.id) {
            when (val citiesResponse = citiesRepository.getCities(countryId)) {
                is Result.Success -> _cities.value = citiesResponse.data.map { it.toEntity() }
                is Result.Error -> showMessage(citiesResponse.getString())
            }
        }
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
        _currentFoundUsersLimit.value = usersCount
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

    private fun showMessage(message: String) {
        Timber.d("showMessage: $message")
        _message.value = Event(message)
    }

    fun onSearchButtonClicked() = viewModelScope.launch {
        val cityId = currentCity.value?.id
        val cityTitle = currentCity.value?.title
        val countryId = currentCountry.value?.peekContent()?.id
        val countryTitle = currentCountry.value?.peekContent()?.title
        val ageFrom = currentAgeFrom.value
        val ageTo = currentAgeTo.value
        val relation = currentRelation.value?.value
        val sex = currentSex.value?.value
        val withPhoneOnly = currentWithPhoneOnly.value
        val foundUsersLimit = currentFoundUsersLimit.value
        val friendsMinCount = currentFriendsMinCount.value
        val friendsMaxCount = currentFriendsMaxCount.value
        val followersMinCount = currentFollowersMinCount.value
        val followersMaxCount = currentFollowersMaxCount.value
        val daysInterval = currentDaysInterval.value
        if (
            cityId != null
            && cityTitle != null
            && countryId != null
            && countryTitle != null
            && sex != null
            && withPhoneOnly != null
            && foundUsersLimit != null
            && followersMinCount != null
            && followersMaxCount != null
            && daysInterval != null
        ) {
            val newSearchId = searchesRepository.insertSearch(
                Search(
                    cityId,
                    cityTitle,
                    countryId,
                    countryTitle,
                    ageFrom,
                    ageTo,
                    relation,
                    sex,
                    withPhoneOnly,
                    foundUsersLimit,
                    friendsMinCount,
                    friendsMaxCount,
                    followersMinCount,
                    followersMaxCount,
                    daysInterval
                )
            )
            _newSearchId.value = Event(newSearchId)
        }
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}