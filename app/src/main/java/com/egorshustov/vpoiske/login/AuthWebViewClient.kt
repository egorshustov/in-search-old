package com.egorshustov.vpoiske.login

import android.net.http.SslError
import android.os.Build
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.login.LoginFragment.Companion.JS_INTERFACE_NAME

class AuthWebViewClient : WebViewClient() {

    private var authWebViewClientCallback: AuthWebViewClientCallback? = null

    override fun onPageFinished(view: WebView?, url: String?) {
        view?.loadUrl(
            "javascript:window.$JS_INTERFACE_NAME.showHTML" +
                    "('$url', '<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');"
        )
        super.onPageFinished(view, url)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        authWebViewClientCallback?.onReceivedError(
            view?.context?.getString(R.string.error_something_went_wrong).orEmpty()
        )
        super.onReceivedSslError(view, handler, error)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (request?.isForMainFrame == true) {
            @StringRes
            val errorStringRes = if (error?.errorCode == ERROR_HOST_LOOKUP) {
                R.string.error_check_internet_or_unavailable
            } else {
                R.string.error_something_went_wrong
            }
            authWebViewClientCallback?.onReceivedError(
                view?.context?.getString(errorStringRes).orEmpty()
            )
        }
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        if (request?.isForMainFrame == true) {
            authWebViewClientCallback?.onReceivedError(
                view?.context?.getString(R.string.error_something_went_wrong).orEmpty()
            )
        }
        super.onReceivedHttpError(view, request, errorResponse)
    }

    interface AuthWebViewClientCallback {
        fun onReceivedError(errorText: String)
    }

    fun setAuthWebViewClientCallback(authWebViewClientCallback: AuthWebViewClientCallback) {
        this.authWebViewClientCallback = authWebViewClientCallback
    }
}