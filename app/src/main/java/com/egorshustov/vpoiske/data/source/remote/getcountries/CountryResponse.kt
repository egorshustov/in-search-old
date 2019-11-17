package com.egorshustov.vpoiske.data.source.remote.getcountries

import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.util.NO_VALUE

data class CountryResponse(
    val id: Int?,
    val title: String?
) {
    fun toEntity() = Country(id ?: NO_VALUE, title ?: "")
}