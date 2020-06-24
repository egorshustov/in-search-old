package com.egorshustov.vpoiske.domain.users

import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.util.Credentials
import com.egorshustov.vpoiske.util.DEFAULT_API_VERSION
import com.egorshustov.vpoiske.util.DEFAULT_GET_USER_FIELDS
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(
        userId: Long,
        fields: String = DEFAULT_GET_USER_FIELDS,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken
    ): Result<UserResponse> =
        usersRepository.getUser(userId, fields, apiVersion, accessToken)
}