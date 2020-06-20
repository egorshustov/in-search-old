package com.egorshustov.vpoiske.searchprocessservice

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.*
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchProcessService : LifecycleService() {

    private val binder = SearchProcessBinder()

    private var searchJob: Job? = null

    private val _isSearchRunning = MutableLiveData(false)
    val isSearchRunning: LiveData<Boolean> = _isSearchRunning.distinctUntilChanged()

    @Inject
    lateinit var interactor: SearchProcessServiceInteractor

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        observeInteractorLiveData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand")
        _isSearchRunning.value = searchJob?.isActive == true
        return super.onStartCommand(intent, flags, startId)
    }

    private fun observeInteractorLiveData() = with(interactor) {
        message.observe(this@SearchProcessService, EventObserver { showMessage(it) })
    }

    fun startSearch(searchId: Long) {
        Timber.d("onCreate")
        searchJob = lifecycleScope.launch { interactor.onSearchStarted(searchId) }.apply {
            invokeOnCompletion { _isSearchRunning.value = false }
        }
    }

    fun stopSearch() {
        Timber.d("stopSearch")
        searchJob?.cancel()
    }

    inner class SearchProcessBinder : Binder() {
        fun getService(): SearchProcessService = this@SearchProcessService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Timber.d("onBind")
        return binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Timber.d("onRebind")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Timber.d("onTaskRemoved")
        super.onTaskRemoved(rootIntent)
    }

    override fun onTrimMemory(level: Int) {
        Timber.d("onTrimMemory")
        super.onTrimMemory(level)
    }

    override fun onLowMemory() {
        Timber.d("onLowMemory")
        super.onLowMemory()
    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        super.onDestroy()
    }
}