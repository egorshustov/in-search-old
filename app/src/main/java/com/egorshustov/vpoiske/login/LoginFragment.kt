package com.egorshustov.vpoiske.login

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentLoginBinding
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override val viewModel by activityViewModels<LoginViewModel> { viewModelFactory }

    private val authWebViewClient = AuthWebViewClient()

    private var currentRequestType = AuthRequestTypes.IMPLICIT_FLOW
    private var login = ""
    private var password = ""

    private val appJsInterfaceCallback = object : AppJsInterface.AppJsInterfaceCallback {

        override fun onPageObtained(url: String, html: String) = with(binding) {
            when (currentRequestType) {
                AuthRequestTypes.IMPLICIT_FLOW -> {
                    if (html.contains("name=\"email\"")) {
                        lifecycleScope.launch {
                            webViewAuth.evaluateJavascript(
                                "javascript:document.querySelector('dl.fi_row:nth-child(5) > dd:nth-child(2) > div:nth-child(1) > input:nth-child(1)').value='$login';" +
                                        "document.querySelector('dl.fi_row:nth-child(6) > dd:nth-child(2) > div:nth-child(1) > input:nth-child(1)').value='$password';" +
                                        "document.querySelector('input.button').click()", null
                            )
                        }
                        return
                    }
                    if (html.contains("Security Error")) {
                        currentRequestType = AuthRequestTypes.AUTHORIZATION_CODE_FLOW
                        lifecycleScope.launch { webViewAuth.loadUrl(urlToLoad2) }
                        return
                    }
                    val uri = Uri.parse(url.replace("#", "?"))
                    val accessToken = uri.getQueryParameter("access_token")
                    val userId = uri.getQueryParameter("user_id")?.toLong()
                    if (userId != null && accessToken != null) {
                        lifecycleScope.launch {
                            viewModel.onAuthDataObtained(userId, accessToken)
                            findNavController().popBackStack(R.id.mainFragment, false)
                        }
                        return
                    }
                }
                AuthRequestTypes.AUTHORIZATION_CODE_FLOW -> {
                    if (html.contains("Code is invalid or expired")) {
                        showErrorMessage()
                        return
                    }

                    val uri = Uri.parse(url.replace("#", "?"))
                    val code = uri.getQueryParameter("code")
                    code?.let {
                        val urlToLoad3 =
                            "https://oauth.vk.com/access_token?client_id=$CLIENT_ID" +
                                    "&scope=$SCOPE" +
                                    "&client_secret=$CLIENT_SECRET" +
                                    "&redirect_uri=$REDIRECT_URL" +
                                    "&display=mobile" +
                                    "&code=$it"
                        lifecycleScope.launch { webViewAuth.loadUrl(urlToLoad3) }
                        return
                    }
                }
            }
            if (html.contains("class=\"button\" type=\"submit\"")) {
                lifecycleScope.launch { webViewAuth.loadUrl("javascript:document.querySelector('.button').click()") }
                return
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLogin.setText(VK_LOGIN) // todo remove
        binding.editPassword.setText(VK_PASSWORD) // todo remove
        setButtonAuthListener()
        setAuthWebViewClientCallback()
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webViewAuth.apply {
            settings.javaScriptEnabled = true
            webViewClient = authWebViewClient
            val appJsInterface = AppJsInterface()
            appJsInterface.setAppJsInterfaceCallback(appJsInterfaceCallback)
            addJavascriptInterface(appJsInterface, JS_INTERFACE_NAME)
        }
    }

    private fun setButtonAuthListener() = with(binding) {
        buttonAuth.setOnClickListener {
            //todo trim and check credentials
            viewModel.isRequestProcessing.value = true
            login = editLogin.text.toString()
            password = editPassword.text.toString()
            webViewAuth.loadUrl(urlToLoad)
        }
    }

    private fun setAuthWebViewClientCallback() {
        authWebViewClient.setAuthWebViewClientCallback(object :
            AuthWebViewClient.AuthWebViewClientCallback {

            override fun onReceivedError(errorText: String) = showErrorMessage(errorText)
        })
    }

    private fun showErrorMessage(errorText: String = "") {
        viewModel.isRequestProcessing.value = false
        if (errorText.isNotBlank()) {
            requireContext().showMessage(errorText)
        } else {
            requireContext().showMessage(getString(R.string.error_something_went_wrong))
        }
    }

    companion object {
        val JS_INTERFACE_NAME = AppJsInterface::javaClass.name

        private const val CLIENT_ID = 6604827
        private const val CLIENT_SECRET = "KfFi8pmxRpGDLdmlqruf"
        private const val SCOPE = "messages,offline"
        private const val REDIRECT_URL = "https://oauth.vk.com/blank.html"

        private const val urlToLoad = "https://oauth.vk.com/authorize?client_id=$CLIENT_ID" +
                "&scope=$SCOPE" +
                "&redirect_uri=$REDIRECT_URL" +
                "&display=mobile" +
                "&v=$DEFAULT_API_VERSION" +
                "&response_type=token&revoke=1"

        private const val urlToLoad2 =
            "https://oauth.vk.com/authorize?client_id=$CLIENT_ID" +
                    "&scope=$SCOPE" +
                    "&redirect_uri=$REDIRECT_URL" +
                    "&display=mobile" +
                    "&v=$DEFAULT_API_VERSION" +
                    "&response_type=code&revoke=1"
    }

}