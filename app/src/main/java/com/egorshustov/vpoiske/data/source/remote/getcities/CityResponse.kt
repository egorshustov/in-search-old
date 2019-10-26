package com.egorshustov.vpoiske.data.source.remote.getcities

import com.egorshustov.vpoiske.data.City

data class CityResponse(
    val id: Int?,
    val title: String?,
    val area: String?,
    val region: String?
) {
    fun toEntity() = City(
        id ?: -1,
        title ?: "",
        area ?: "",
        region ?: "",
        false
    )
}