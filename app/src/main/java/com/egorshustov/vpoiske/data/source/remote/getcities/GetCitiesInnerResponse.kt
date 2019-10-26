package com.egorshustov.vpoiske.data.source.remote.getcities

import com.google.gson.annotations.SerializedName

data class GetCitiesInnerResponse(
    val count: Int?,
    @SerializedName("items")
    val cityResponseList: List<CityResponse>?
)