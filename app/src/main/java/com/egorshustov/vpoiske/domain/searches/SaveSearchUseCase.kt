package com.egorshustov.vpoiske.domain.searches

import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class SaveSearchUseCase @Inject constructor(
    private val searchesRepository: SearchesRepository
) {

    suspend operator fun invoke(search: Search): Long = searchesRepository.saveSearch(search)
}