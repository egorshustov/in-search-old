package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.google.gson.annotations.SerializedName

data class LastSeen(
    @SerializedName("time")
    val timeUnixSeconds: Int?,
    val platform: Int?
)