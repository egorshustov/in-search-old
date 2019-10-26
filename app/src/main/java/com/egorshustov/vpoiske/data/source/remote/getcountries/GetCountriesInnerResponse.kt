package com.egorshustov.vpoiske.data.source.remote.getcountries

import com.google.gson.annotations.SerializedName

data class GetCountriesInnerResponse(
    val count: Int?,
    @SerializedName("items")
    val countryResponseList: List<CountryResponse>?
)