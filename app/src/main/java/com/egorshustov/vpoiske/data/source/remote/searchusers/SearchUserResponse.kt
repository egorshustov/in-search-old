package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    val id: Int?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("is_closed")
    val isClosed: Boolean?,
    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean?,
    @SerializedName("mobile_phone")
    val mobilePhone: String?,
    @SerializedName("home_phone")
    val homePhone: String?,
    @SerializedName("last_seen")
    val lastSeen: LastSeen?,
    @SerializedName("followers_count")
    val followersCount: Int?
) {
    fun hasCorrectPhone() = isMobilePhoneCorrect() || isHomePhoneCorrect()

    private fun isMobilePhoneCorrect(): Boolean {
        return true
    }

    private fun isHomePhoneCorrect(): Boolean {
        return true
    }
}