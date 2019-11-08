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
        cityId: Int,
        ageFrom: Int,
        ageTo: Int,
        birthDay: Int,
        birthMonth: Int,
        sex: Int = Sex.FEMALE.value,
        hasPhoto: Int = HasPhoto.NECESSARY.value,
        fields: String = DEFAULT_SEARCH_USERS_FIELDS,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = ACCESS_TOKEN,
        count: Int = DEFAULT_SEARCH_USERS_COUNT,
        sortType: Int = SortType.BY_REGISTRATION_DATE.value
    ) = usersRemoteDataSource.searchUsers(
        cityId,
        ageFrom,
        ageTo,
        birthDay,
        birthMonth,
        sex,
        hasPhoto,
        fields,
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