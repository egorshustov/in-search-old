package com.egorshustov.vpoiske.analytics

class FakeVPoiskeAnalyticsImpl : VPoiskeAnalytics {

    override fun loginScreenOpened() {}
    override fun authClicked() {}
    override fun authSuccessful() {}

    override fun newSearchScreenOpened() {}

    override fun startSearchClicked(
        countryTitle: String,
        cityTitle: String,
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
    }

    override fun stopSearchClicked(fromNotification: Boolean) {}

    override fun searchCompleted(foundUsersCount: Int, foundUsersLimit: Int) {}

    override fun onUserClicked() {}

    override fun onSearchHistoryScreenOpened() {}

    override fun onPastSearchScreenOpened() {}

    override fun onChangeUsersViewClicked(columnCount: Int) {}

    override fun onChangeAppThemeClicked(themeName: String) {}

    override fun errorOccurred(message: String, exception: String?, vkErrorCode: Int?) {}
}