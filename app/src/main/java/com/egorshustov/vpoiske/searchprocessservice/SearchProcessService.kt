package com.egorshustov.vpoiske.searchprocessservice

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.VPoiskeActivity
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.NOTIFICATION_CHANNEL_ID
import com.egorshustov.vpoiske.util.SEARCH_NOTIFICATION_ID
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

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        observeInteractorLiveData()
        prepareNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand")
        _isSearchRunning.value = searchJob?.isActive == true
        when (intent?.action) {
            ACTION_STOP -> {
                stopSearch()
                stopForeground(true)
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun observeInteractorLiveData() = with(interactor) {
        message.observe(this@SearchProcessService, EventObserver { showMessage(it) })
        foundUsersCountUpdated.observe(this@SearchProcessService) { foundUsersCount ->
            interactor.foundUsersLimit?.let { foundUsersLimit ->
                sendProgressNotification(foundUsersCount, foundUsersLimit)
            }
        }
    }

    private fun prepareNotification() {
        val stopServiceIntent =
            Intent(this, SearchProcessService::class.java).setAction(ACTION_STOP)
        val stopServicePendingIntent = PendingIntent.getService(this, 0, stopServiceIntent, 0)

        val openActivityIntent = Intent(this, VPoiskeActivity::class.java)
        val openActivityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Выполняется поиск")
            .setContentIntent(openActivityPendingIntent)
            .setSmallIcon(R.drawable.ic_search)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_stop, "Отмена", stopServicePendingIntent)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun startSearch(searchId: Long) {
        Timber.d("onCreate")
        startForeground(SEARCH_NOTIFICATION_ID, notificationBuilder.build())
        notificationManager.notify(SEARCH_NOTIFICATION_ID, notificationBuilder.build())
        searchJob = lifecycleScope.launch { interactor.onSearchStarted(searchId) }.apply {
            invokeOnCompletion { _isSearchRunning.value = false }
        }
    }

    private fun sendProgressNotification(foundUsersCount: Int, foundUsersLimit: Int) {
        notificationBuilder.setProgress(foundUsersLimit, foundUsersCount, false)
            .setContentText("Найдено $foundUsersCount/$foundUsersLimit")
        notificationManager.notify(SEARCH_NOTIFICATION_ID, notificationBuilder.build())
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

    companion object {
        const val ACTION_STOP = "com.egorshustov.vpoiske.searchprocessservice.ACTION_STOP"
    }
}