package com.egorshustov.vpoiske.data.source.remote.getcities

import com.egorshustov.vpoiske.base.BaseVkResponse
import com.egorshustov.vpoiske.data.source.remote.VkErrorResponse

data class GetCitiesResponse(
    val response: GetCitiesInnerResponse?,
    override val error: VkErrorResponse?
) : BaseVkResponse()