package com.egorshustov.vpoiske.analytics

import com.amplitude.api.Amplitude
import com.amplitude.api.Revenue
import org.json.JSONObject
import timber.log.Timber
import java.util.*

abstract class AmplitudeLogger : EventLogger {

    internal abstract val type: String

    override fun logEvent(eventName: String, params: Map<String, Any>) {
        Timber.d("logEvent: $eventName, params: $params")
        if (params.isEmpty()) {
            Amplitude.getInstance(type).logEvent(eventName)
        } else {
            Amplitude.getInstance(type).logEvent(eventName, JSONObject(params))
        }
    }

    override fun logRevenue(
        price: Double,
        currency: Currency,
        revenueType: String,
        productId: String,
        eventProperties: JSONObject
    ) {
        Timber.d("logRevenue: $price, ${currency.currencyCode}, $revenueType, $productId, $eventProperties")
        Amplitude.getInstance(type).logRevenueV2(
            Revenue()
                .setPrice(price)
                .setRevenueType(revenueType)
                .setProductId(productId)
                .setEventProperties(eventProperties)
        )
    }

    class VPoiske : AmplitudeLogger() {

        override val type = AmplitudeMain.Instances.AMPLITUDE_V_POISKE_INSTANCE.name
    }

    // Other classes can be also added here if necessary
}