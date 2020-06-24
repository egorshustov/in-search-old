package com.egorshustov.vpoiske.domain.searches

import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class SaveSearchStartUnixSecondsUseCase @Inject constructor(private val searchesRepository: SearchesRepository) {

    suspend operator fun invoke(id: Long, startUnixSeconds: Int) =
        searchesRepository.saveSearchStartUnixSeconds(id, startUnixSeconds)
}