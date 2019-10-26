package com.egorshustov.vpoiske.data.source.remote.getcountries

import com.egorshustov.vpoiske.data.Country

data class CountryResponse(
    val id: Int?,
    val title: String?
) {
    fun toEntity() = Country(id ?: -1, title ?: "")
}