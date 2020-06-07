package com.egorshustov.vpoiske.login

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.main.AuthenticationState
import com.egorshustov.vpoiske.util.DelegatedPreference
import com.egorshustov.vpoiske.util.PREF_KEY_ACCESS_TOKEN
import javax.inject.Inject

class LoginViewModel @Inject constructor(sharedPreferences: SharedPreferences) : ViewModel() {

    val isRequestProcessing = MutableLiveData(false)

    var accessToken by DelegatedPreference(sharedPreferences, PREF_KEY_ACCESS_TOKEN, "")
        private set

    val authenticationState = MutableLiveData<AuthenticationState>()

    init {
        authenticationState.value = if (accessToken.isNotBlank()) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun onAuthDataObtained(userId: Long, accessToken: String) {
        //this.accessToken = accessToken
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }
}