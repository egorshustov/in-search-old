package com.egorshustov.vpoiske.analytics

import com.egorshustov.vpoiske.BuildConfig
import com.egorshustov.vpoiske.util.NO_VALUE

interface VPoiskeAnalytics {

    fun loginScreenOpened()

    fun authClicked()

    fun authSuccessful()

    fun newSearchScreenOpened()

    fun startSearchClicked(
        countryTitle: String,
        cityTitle: String,
        homeTown: String,
        sex: String,
        ageFrom: Int?,
        ageTo: Int?,
        relation: String,
        withPhoneOnly: Boolean,
        foundUsersLimit: Int,
        daysInterval: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int
    )

    fun stopSearchClicked(fromNotification: Boolean)

    fun searchCompleted(foundUsersCount: Int, foundUsersLimit: Int)

    fun onUserClicked()

    fun onSearchHistoryScreenOpened()

    fun onPastSearchScreenOpened()

    fun onChangeUsersViewClicked(columnCount: Int)

    fun onChangeAppThemeClicked(themeName: String)

    fun errorOccurred(message: String, exception: String? = null, vkErrorCode: Int? = null)
}

class VPoiskeAnalyticsImpl(vPoiskeAmplitudeLogger: EventLogger) : VPoiskeAnalytics {

    private val vPoiskePrefixAmplitudeLogger =
        vPoiskeAmplitudeLogger.withPrefix(if (BuildConfig.DEBUG) "VPoiske: " else "")

    override fun loginScreenOpened() {
        vPoiskePrefixAmplitudeLogger.logEvent("Login Screen Opened")
    }

    override fun authClicked() {
        vPoiskePrefixAmplitudeLogger.logEvent("Authorize Clicked")
    }

    override fun authSuccessful() {
        vPoiskePrefixAmplitudeLogger.logEvent("Authorization Successful")
    }

    override fun newSearchScreenOpened() {
        vPoiskePrefixAmplitudeLogger.logEvent("New Search Screen Opened")
    }

    override fun startSearchClicked(
        countryTitle: String,
        cityTitle: String,
        homeTown: String,
        sex: String,
        ageFrom: Int?,
        ageTo: Int?,
        relation: String,
        withPhoneOnly: Boolean,
        foundUsersLimit: Int,
        daysInterval: Int,
        friendsMinCount: Int?,
        friendsMaxCount: Int?,
        followersMinCount: Int,
        followersMaxCount: Int
    ) {
        val params = mapOf<String, Any>(
            "Country" to countryTitle,
            "City" to cityTitle,
            "Hometown" to homeTown,
            "Sex" to sex,
            "Age From" to (ageFrom ?: NO_VALUE),
            "Age To" to (ageTo ?: NO_VALUE),
            "Relation" to relation,
            "With Phone Only" to if (withPhoneOnly) "Yes" else "No",
            "Desired Users Count" to foundUsersLimit,
            "Days Interval" to daysInterval,
            "Friends Min Count" to (friendsMinCount ?: NO_VALUE),
            "Friends Max Count" to (friendsMaxCount ?: NO_VALUE),
            "Followers Min Count" to followersMinCount,
            "Followers Max Count" to followersMaxCount
        )
        vPoiskePrefixAmplitudeLogger.logEvent("Start Search Clicked", params)
    }

    override fun stopSearchClicked(fromNotification: Boolean) {
        val params = mapOf("From Notification" to if (fromNotification) "Yes" else "No")
        vPoiskePrefixAmplitudeLogger.logEvent("Stop Search Clicked", params)
    }

    override fun searchCompleted(foundUsersCount: Int, foundUsersLimit: Int) {
        val params =
            mapOf("Found Users Count" to foundUsersCount, "Desired Users Count" to foundUsersLimit)
        vPoiskePrefixAmplitudeLogger.logEvent("Search Completed", params)
    }

    override fun onUserClicked() {
        vPoiskePrefixAmplitudeLogger.logEvent("User Clicked")
    }

    override fun onSearchHistoryScreenOpened() {
        vPoiskePrefixAmplitudeLogger.logEvent("Search History Screen Opened")
    }

    override fun onPastSearchScreenOpened() {
        vPoiskePrefixAmplitudeLogger.logEvent("Past Search Screen Opened")
    }

    override fun onChangeUsersViewClicked(columnCount: Int) {
        val params = mapOf("Columns Count" to columnCount)
        vPoiskePrefixAmplitudeLogger.logEvent("Users View Changed", params)
    }

    override fun onChangeAppThemeClicked(themeName: String) {
        val params = mapOf("Name" to themeName)
        vPoiskePrefixAmplitudeLogger.logEvent("App Theme Changed", params)
    }

    override fun errorOccurred(message: String, exception: String?, vkErrorCode: Int?) {
        val params = mutableMapOf<String, Any>("Message" to message)
        if (!exception.isNullOrBlank()) params["Exception"] = exception
        vkErrorCode?.let { params["VK Error Code"] = it }
        vPoiskePrefixAmplitudeLogger.logEvent("Error", params)
    }
}