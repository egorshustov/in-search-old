package com.egorshustov.vpoiske.data.source.remote.getcities

import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.util.NO_VALUE

data class CityResponse(
    val id: Int?,
    val title: String?,
    val area: String?,
    val region: String?
) {
    fun toEntity() = City(
        id ?: NO_VALUE,
        title.orEmpty(),
        area.orEmpty(),
        region.orEmpty()
    )
}