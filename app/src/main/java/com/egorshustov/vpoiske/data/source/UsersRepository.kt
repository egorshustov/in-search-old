package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.source.local.UsersDao
import com.egorshustov.vpoiske.data.source.remote.UsersRemoteDataSource
import com.egorshustov.vpoiske.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersDao: UsersDao,
    private val usersRemoteDataSource: UsersRemoteDataSource
) {
    suspend fun searchUsers(
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
        accessToken: String = ACCESS_TOKEN,
        count: Int = DEFAULT_SEARCH_USERS_COUNT,
        sortType: Int = SortType.BY_REGISTRATION_DATE.value
    ) = usersRemoteDataSource.searchUsers(
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

    suspend fun getUser(
        userId: Int,
        fields: String = DEFAULT_GET_USER_FIELDS,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = ACCESS_TOKEN
    ) = usersRemoteDataSource.getUser(
        userId,
        fields,
        apiVersion,
        accessToken
    )
}