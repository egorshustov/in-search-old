package com.egorshustov.vpoiske.util

import android.webkit.JavascriptInterface

class AppJsInterface {

    private var appJsInterfaceCallback: AppJsInterfaceCallback? = null

    @JavascriptInterface
    fun showHTML(url: String, html: String) {
        appJsInterfaceCallback?.onPageObtained(url, html)
    }

    interface AppJsInterfaceCallback {
        fun onPageObtained(url: String, html: String)
    }

    fun setAppJsInterfaceCallback(appJsInterfaceCallback: AppJsInterfaceCallback) {
        this.appJsInterfaceCallback = appJsInterfaceCallback
    }
}