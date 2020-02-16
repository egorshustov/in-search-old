package com.egorshustov.vpoiske.data.source.remote.getuser

import com.egorshustov.vpoiske.base.BaseVkResponse
import com.egorshustov.vpoiske.data.source.remote.VkErrorResponse
import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("response")
    val userResponseList: List<UserResponse>?,
    override val error: VkErrorResponse?
) : BaseVkResponse()