package com.egorshustov.vpoiske.domain.users

import com.egorshustov.vpoiske.data.source.UsersRepository
import com.egorshustov.vpoiske.data.source.remote.Result
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse
import com.egorshustov.vpoiske.util.*
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(
        countryId: Int,
        cityId: Int,
        ageFrom: Int?,
        ageTo: Int?,
        birthDay: Int,
        birthMonth: Int,
        fields: String,
        relation: Int? = Relation.NOT_DEFINED.value,
        sex: Int = Sex.FEMALE.value,
        hasPhoto: Int = HasPhoto.NECESSARY.value,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken,
        count: Int = DEFAULT_SEARCH_USERS_COUNT,
        sortType: Int = SortType.BY_REGISTRATION_DATE.value
    ): Result<SearchUsersInnerResponse> =
        usersRepository.searchUsers(
            countryId,
            cityId,
            ageFrom,
            ageTo,
            birthDay,
            birthMonth,
            fields,
            relation,
            sex,
            hasPhoto,
            apiVersion,
            accessToken,
            count,
            sortType
        )
}