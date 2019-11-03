package com.egorshustov.vpoiske.newsearch

import androidx.lifecycle.MutableLiveData
import com.egorshustov.vpoiske.base.BaseState
import com.egorshustov.vpoiske.util.DEFAULT_CITY
import com.egorshustov.vpoiske.util.DEFAULT_COUNTRY
import com.egorshustov.vpoiske.util.Event

class NewSearchState: BaseState() {
    val country = MutableLiveData(DEFAULT_COUNTRY)
    val city = MutableLiveData(DEFAULT_CITY)
    val snackBarMessage = MutableLiveData<Event<String>>()
    val resetCityCommand = MutableLiveData<Event<Unit>>()
}