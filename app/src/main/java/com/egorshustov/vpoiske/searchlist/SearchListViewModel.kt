package com.egorshustov.vpoiske.searchlist

import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class SearchListViewModel @Inject constructor(
) : ViewModel() {
    init {
        Timber.d("%s init", toString())
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}