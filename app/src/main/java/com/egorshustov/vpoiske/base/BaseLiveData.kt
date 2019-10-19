package com.egorshustov.vpoiske.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class BaseLiveData<State>(private val state: State) : MutableLiveData<State>() {

    var hasBeenHandled = false
        private set

    fun getStateIfNotHandled(): State? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            state
        }
    }

    fun peekState(): State = state

}

class StateObserver<State>(private val onEventUnhandledState: (State) -> Unit) :
    Observer<BaseLiveData<State>> {
    override fun onChanged(baseLiveData: BaseLiveData<State>?) {
        baseLiveData?.getStateIfNotHandled()?.let {
            onEventUnhandledState(it)
        }
    }
}