package com.egorshustov.vpoiske.login

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.egorshustov.vpoiske.analytics.AmplitudeMain
import com.egorshustov.vpoiske.analytics.VPoiskeAnalytics
import com.egorshustov.vpoiske.main.AuthenticationState
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DelegatedPreference
import com.egorshustov.vpoiske.util.PREF_KEY_ACCESS_TOKEN

class LoginViewModel @ViewModelInject constructor(
    sharedPreferences: SharedPreferences,
    private val vPoiskeAnalytics: VPoiskeAnalytics
) : ViewModel() {

    val isRequestProcessing = MutableLiveData(false)

    val loginText = MutableLiveData("")

    val passwordText = MutableLiveData("")

    val areCredentialsValid: LiveData<Boolean> = loginText.switchMap { loginText ->
        passwordText.switchMap { passwordText ->
            liveData { emit(areCredentialsValid(loginText, passwordText)) }
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

    fun onLoginFragmentViewCreated() {
        vPoiskeAnalytics.loginScreenOpened()
    }

    fun onAuthButtonClicked() {
        vPoiskeAnalytics.authClicked()
    }

    fun onAuthDataObtained(userId: Long, accessToken: String) {
        vPoiskeAnalytics.authSuccessful()
        AmplitudeMain.setUserId(AmplitudeMain.generateAmplitudeUserIdByVkId(userId))
        this.accessToken = accessToken
        Credentials.accessToken = accessToken
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }

    fun onErrorMessageShown(message: String) {
        vPoiskeAnalytics.errorOccurred(message)
    }

    private fun areCredentialsValid(login: String, password: String): Boolean =
        login.isNotBlank() && password.isNotBlank()
}