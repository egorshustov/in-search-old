package com.egorshustov.vpoiske.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.main.AuthenticationState
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DelegatedPreference
import com.egorshustov.vpoiske.util.PREF_KEY_ACCESS_TOKEN
import com.egorshustov.vpoiske.util.switchMap
import javax.inject.Inject

class LoginViewModel @Inject constructor(sharedPreferences: SharedPreferences) : ViewModel() {

    val isRequestProcessing = MutableLiveData(false)

    val loginText = MutableLiveData("")

    val passwordText = MutableLiveData("")

    val areCredentialsValid: LiveData<Boolean> = loginText.switchMap { loginText ->
        passwordText.switchMap { passwordText ->
            MutableLiveData<Boolean>().apply {
                value = areCredentialsValid(loginText, passwordText)
            }
        }
    }

    var accessToken by DelegatedPreference(sharedPreferences, PREF_KEY_ACCESS_TOKEN, "")
        private set

    val authenticationState = MutableLiveData<AuthenticationState>()

    init {
        authenticationState.value = if (accessToken.isNotBlank()) {
            Credentials.accessToken = accessToken
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun onAuthDataObtained(userId: Long, accessToken: String) {
        this.accessToken = accessToken
        Credentials.accessToken = accessToken
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }

    private fun areCredentialsValid(login: String, password: String): Boolean =
        login.isNotBlank() && password.isNotBlank()
}