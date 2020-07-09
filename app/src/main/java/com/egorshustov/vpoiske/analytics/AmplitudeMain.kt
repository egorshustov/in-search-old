package com.egorshustov.vpoiske.analytics

import android.app.Application
import com.amplitude.api.Amplitude
import com.egorshustov.vpoiske.BuildConfig
import com.egorshustov.vpoiske.util.toMd5Hash
import java.util.*

object AmplitudeMain {

    enum class Instances(val apiKey: String) {

        AMPLITUDE_V_POISKE_INSTANCE(if (BuildConfig.DEBUG) "d735b337b0b3404bc7faa7ef1d58881e" else "26f20d07f43e8d1d3d9bb764bff2441b")
        // Other instances can be also added here if necessary
    }

    fun init(application: Application) {
        Instances.values().forEach {
            Amplitude.getInstance(it.name).initialize(application, it.apiKey)
                .useAdvertisingIdForDeviceId()
                .enableForegroundTracking(application)
        }
    }

    fun generateAmplitudeUserIdByVkId(vkId: Long): String =
        "QV5JibXagTEZwkTaF0${vkId}WoRGjr".toMd5Hash().toUpperCase(Locale.ENGLISH)

    fun setUserId(userId: String) =
        Instances.values().forEach { Amplitude.getInstance(it.name).userId = userId }
}