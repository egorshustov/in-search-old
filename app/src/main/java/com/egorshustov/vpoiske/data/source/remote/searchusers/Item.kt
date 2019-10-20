package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.google.gson.annotations.SerializedName

data class Item(
    val id: Int,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("is_closed")
    val isClosed: String?,
    @SerializedName("can_access_closed")
    val canAccessClosed: String?,
    @SerializedName("mobile_phone")
    val mobilePhone: String?,
    @SerializedName("home_phone")
    val homePhone: String?,
    @SerializedName("last_seen")
    val lastSeen: LastSeen?
)