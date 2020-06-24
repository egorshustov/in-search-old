package com.egorshustov.vpoiske.domain.users

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.UsersRepository
import javax.inject.Inject

class SaveUsersUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(userList: List<User>): List<Long> =
        usersRepository.saveUsers(userList)
}