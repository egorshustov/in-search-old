package com.egorshustov.vpoiske.data.source.remote

import com.google.gson.annotations.SerializedName

data class VkErrorResponse(
    @SerializedName("error_code")
    val errorCode: Int?,
    @SerializedName("error_msg")
    val errorMessage: String?
)