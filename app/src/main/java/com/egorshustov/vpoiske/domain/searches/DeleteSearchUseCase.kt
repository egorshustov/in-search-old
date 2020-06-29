package com.egorshustov.vpoiske.domain.searches

import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class DeleteSearchUseCase @Inject constructor(
    private val searchesRepository: SearchesRepository
) {

    suspend operator fun invoke(id: Long) = searchesRepository.deleteSearch(id)
}