package com.egorshustov.vpoiske.domain.searches

import com.egorshustov.vpoiske.data.source.SearchesRepository
import com.egorshustov.vpoiske.data.source.UsersRepository
import javax.inject.Inject

class DeleteSearchWithUsersUseCase @Inject constructor(
    private val searchesRepository: SearchesRepository,
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(searchId: Long) {
        searchesRepository.deleteSearch(searchId)
        usersRepository.deleteUsersFromSearch(searchId)
    }
}