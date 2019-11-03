package com.egorshustov.vpoiske.searchlist

import com.egorshustov.vpoiske.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class SearchListViewModel @Inject constructor(
) : BaseViewModel<SearchListState>(SearchListState()) {
    init {
        Timber.d("%s init", toString())
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}