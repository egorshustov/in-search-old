package com.egorshustov.vpoiske.searchlog

import com.egorshustov.vpoiske.base.BaseViewModel
import javax.inject.Inject

class SearchLogViewModel @Inject constructor(
) : BaseViewModel<SearchLogState>(SearchLogState()) {
    init {
        val h = 9
    }

    override fun onCleared() {
        val jj = 0
        super.onCleared()
    }
}