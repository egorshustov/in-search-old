package com.egorshustov.vpoiske.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    val isRequestProcessing = MutableLiveData(false)
}