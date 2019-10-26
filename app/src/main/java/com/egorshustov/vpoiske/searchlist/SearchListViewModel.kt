package com.egorshustov.vpoiske.searchlist

import com.egorshustov.vpoiske.base.BaseViewModel
import javax.inject.Inject

class SearchListViewModel @Inject constructor(
) : BaseViewModel<SearchListState>(SearchListState()) {
    init {
        val h = 9
    }

    override fun onCleared() {
        val jj = 0
        super.onCleared()
    }
}