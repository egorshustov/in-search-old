package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.google.gson.annotations.SerializedName

data class SearchUsersInnerResponse(
    val count: Int?,
    @SerializedName("items")
    val searchUserResponseList: List<SearchUserResponse>?
)