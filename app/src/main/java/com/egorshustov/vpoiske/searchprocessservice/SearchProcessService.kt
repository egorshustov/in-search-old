package com.egorshustov.vpoiske.searchprocessservice

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.VPoiskeActivity
import com.egorshustov.vpoiske.util.*
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

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        observeInteractorLiveData()
        prepareNotification()
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
            .addAction(R.drawable.ic_stop, "Отмена", stopServicePendingIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand")
        _isSearchRunning.value = searchJob?.isActive == true
        when (intent?.action) {
            ACTION_STOP -> {
                interactor.onStopSearchClicked()
                stopSearch()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun startSearch(searchId: Long) {
        Timber.d("startSearch")
        _isSearchRunning.value = true
        startForeground(PROGRESS_NOTIFICATION_ID, notificationBuilder.build())
        NotificationManagerCompat.from(this)
            .notify(PROGRESS_NOTIFICATION_ID, notificationBuilder.build())
        searchJob = lifecycleScope.launch { interactor.onSearchStarted(searchId) }.apply {
            invokeOnCompletion {
                _isSearchRunning.value = false
                showMessage("Поиск завершён")
                NotificationManagerCompat.from(this@SearchProcessService).cancel(
                    PROGRESS_NOTIFICATION_ID
                )
                interactor.onSearchCompleted()
                sendCompleteNotification(
                    interactor.foundUsersCountUpdated.value ?: 0,
                    interactor.foundUsersLimit ?: 0
                )
                stopForeground(true)
                stopSelf()
            }
        }
    }

    private fun observeInteractorLiveData() = with(interactor) {
        message.observe(this@SearchProcessService, EventObserver { showMessage(it) })
        foundUsersCountUpdated.observe(this@SearchProcessService) { foundUsersCount ->
            interactor.foundUsersLimit?.let { sendProgressNotification(foundUsersCount, it) }
        }
    }

    private fun sendProgressNotification(foundUsersCount: Int, foundUsersLimit: Int) {
        notificationBuilder
            .setContentText("Найдено $foundUsersCount/$foundUsersLimit")
            .setProgress(foundUsersLimit, foundUsersCount, false)
        NotificationManagerCompat.from(this)
            .notify(PROGRESS_NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun sendCompleteNotification(foundUsersCount: Int, foundUsersLimit: Int) {
        notificationBuilder.setContentTitle("Поиск завершён")
            .setContentText("Найдено $foundUsersCount/$foundUsersLimit")
            .setOnlyAlertOnce(false)
            .setAutoCancel(true)
            .setProgress(0, 0, false)
            .mActions.clear()
        NotificationManagerCompat.from(this)
            .notify(COMPLETE_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun stopSearch() {
        Timber.d("stopSearch")
        searchJob?.cancel()
        stopForeground(true)
        stopSelf()
    }

    inner class SearchProcessBinder : Binder() {
        fun getService(): SearchProcessService = this@SearchProcessService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Timber.d("onBind")
        return binder
    }

    companion object {
        const val ACTION_STOP = "com.egorshustov.vpoiske.searchprocessservice.ACTION_STOP"
    }
}