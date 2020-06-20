package com.egorshustov.vpoiske

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.egorshustov.vpoiske.util.NOTIFICATION_CHANNEL_DESCRIPTION
import com.egorshustov.vpoiske.util.NOTIFICATION_CHANNEL_ID
import com.egorshustov.vpoiske.util.NOTIFICATION_CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class VPoiskeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vPoiskeNotificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(false)
                enableVibration(false)
                description = NOTIFICATION_CHANNEL_DESCRIPTION
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                vPoiskeNotificationChannel
            )
        }
    }
}