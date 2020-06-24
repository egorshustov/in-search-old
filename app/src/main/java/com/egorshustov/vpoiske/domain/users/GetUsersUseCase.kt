package com.egorshustov.vpoiske.domain.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.UsersRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    operator fun invoke(searchId: Long?): LiveData<List<User>> =
        usersRepository.getUsers().map { users ->
            users.filter { it.searchId == searchId }
        }
}