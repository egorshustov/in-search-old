package com.egorshustov.vpoiske.analytics

import org.json.JSONObject
import java.util.*

interface EventLogger {

    fun logEvent(eventName: String, params: Map<String, Any> = emptyMap())

    fun logRevenue(
        price: Double,
        currency: Currency,
        revenueType: String,
        productId: String,
        eventProperties: JSONObject
    )
}

fun EventLogger.withPrefix(prefix: String) = PrefixEventLogger(prefix, this)

class PrefixEventLogger(
    private val prefix: String,
    private val eventLogger: EventLogger
) : EventLogger {

    override fun logEvent(eventName: String, params: Map<String, Any>) =
        eventLogger.logEvent(prefix + eventName, params)

    override fun logRevenue(
        price: Double,
        currency: Currency,
        revenueType: String,
        productId: String,
        eventProperties: JSONObject
    ) =
        eventLogger.logRevenue(price, currency, revenueType, productId, eventProperties)
}