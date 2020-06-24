package com.egorshustov.vpoiske.domain.searches

import androidx.paging.DataSource
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class GetSearchesWithUsersUseCase @Inject constructor(
    private val searchesRepository: SearchesRepository
) {

    operator fun invoke(): DataSource.Factory<Int, SearchWithUsers> =
        searchesRepository.getSearchesWithUsers()
}