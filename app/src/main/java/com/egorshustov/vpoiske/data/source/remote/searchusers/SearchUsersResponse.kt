package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.egorshustov.vpoiske.base.BaseVkResponse
import com.egorshustov.vpoiske.data.source.remote.VkErrorResponse

data class SearchUsersResponse(
    val response: SearchUsersInnerResponse?,
    override val error: VkErrorResponse?
) : BaseVkResponse()