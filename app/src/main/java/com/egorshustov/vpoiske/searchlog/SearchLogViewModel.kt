package com.egorshustov.vpoiske.searchlog

import com.egorshustov.vpoiske.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class SearchLogViewModel @Inject constructor(
) : BaseViewModel<SearchLogState>(SearchLogState()) {
    init {
        Timber.d("%s init", toString())
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}