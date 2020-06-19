package com.egorshustov.vpoiske.searchprocessservice

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService

class SearchProcessService : LifecycleService() {

    private val binder = SearchProcessBinder()

    private val interactor = SearchProcessServiceInteractor()

    inner class SearchProcessBinder : Binder() {
        fun getService(): SearchProcessService = this@SearchProcessService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }
}