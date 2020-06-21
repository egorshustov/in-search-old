package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.local.UsersDao
import com.egorshustov.vpoiske.data.source.remote.UsersRemoteDataSource
import com.egorshustov.vpoiske.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersDao: UsersDao,
    private val ioDispatcher: CoroutineDispatcher,
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
        accessToken: String = Credentials.accessToken,
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
        userId: Long,
        fields: String = DEFAULT_GET_USER_FIELDS,
        apiVersion: String = DEFAULT_API_VERSION,
        accessToken: String = Credentials.accessToken
    ) = usersRemoteDataSource.getUser(
        userId,
        fields,
        apiVersion,
        accessToken
    )

    suspend fun insertUser(user: User): Long =
        withContext(ioDispatcher) { usersDao.insertUser(user) }

    suspend fun insertUsers(userList: List<User>): List<Long> =
        withContext(ioDispatcher) { usersDao.insertUsers(userList) }

    fun getLiveUsers() = usersDao.getLiveUsers()
}