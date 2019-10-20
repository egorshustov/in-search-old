package com.egorshustov.vpoiske.data.source.remote.getuser

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("response")
    val userResponseList: List<UserResponse>?
)