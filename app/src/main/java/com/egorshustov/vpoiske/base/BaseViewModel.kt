package com.egorshustov.vpoiske.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State>(
    val state: State
) : ViewModel() {

}