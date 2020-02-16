package com.egorshustov.vpoiske.base

import com.egorshustov.vpoiske.data.source.remote.VkErrorResponse

abstract class BaseVkResponse {
    abstract val error: VkErrorResponse?
}