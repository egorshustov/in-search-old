package com.egorshustov.vpoiske.main

import com.egorshustov.vpoiske.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
) : BaseViewModel<MainState>(MainState()) {
    init {
        Timber.d("%s init", toString())
    }

    override fun onCleared() {
        Timber.d("%s cleared", toString())
        super.onCleared()
    }
}