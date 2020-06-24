package com.egorshustov.vpoiske.domain.searches

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class GetLastSearchIdUseCase @Inject constructor(private val searchesRepository: SearchesRepository) {

    operator fun invoke(): LiveData<Long?> = searchesRepository.getLastSearchId()
}