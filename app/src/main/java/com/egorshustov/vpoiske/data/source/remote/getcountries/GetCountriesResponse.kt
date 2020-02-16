package com.egorshustov.vpoiske.data.source.remote.getcountries

import com.egorshustov.vpoiske.base.BaseVkResponse
import com.egorshustov.vpoiske.data.source.remote.VkErrorResponse

data class GetCountriesResponse(
    val response: GetCountriesInnerResponse?,
    override val error: VkErrorResponse?
) : BaseVkResponse()