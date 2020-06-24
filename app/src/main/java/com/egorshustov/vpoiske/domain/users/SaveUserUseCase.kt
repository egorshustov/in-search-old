package com.egorshustov.vpoiske.domain.users

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.UsersRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(user: User): Long = usersRepository.saveUser(user)
}