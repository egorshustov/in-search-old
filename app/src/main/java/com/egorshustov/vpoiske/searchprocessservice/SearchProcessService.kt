package com.egorshustov.vpoiske.searchprocessservice

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchProcessService : LifecycleService() {

    private val binder = SearchProcessBinder()

    private var searchJob: Job? = null

    @Inject
    lateinit var interactor: SearchProcessServiceInteractor

    override fun onCreate() {
        super.onCreate()
        observeInteractorLiveData()
    }

    private fun observeInteractorLiveData() = with(interactor) {
        stopSearch.observe(this@SearchProcessService, EventObserver {
            stopSearch()
        })
        message.observe(this@SearchProcessService, EventObserver { showMessage(it) })
    }

    fun startSearch(searchId: Long) {
        searchJob = lifecycleScope.launch {
            interactor.onSearchStarted(searchId)
        }
    }

    fun stopSearch() {
        searchJob?.cancel()
    }

    inner class SearchProcessBinder : Binder() {
        fun getService(): SearchProcessService = this@SearchProcessService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }
}