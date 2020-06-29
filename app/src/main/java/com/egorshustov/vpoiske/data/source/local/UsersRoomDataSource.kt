package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRoomDataSource @Inject constructor(
    private val usersDao: UsersDao,
    private val ioDispatcher: CoroutineDispatcher
) : UsersLocalDataSource {

    override fun getUsers(): LiveData<List<User>> = usersDao.getLiveUsers()

    override suspend fun deleteUsersFromSearch(searchId: Long) = withContext(ioDispatcher) {
        usersDao.deleteUsersFromSearch(searchId)
    }

    override suspend fun saveUser(user: User): Long = withContext(ioDispatcher) {
        usersDao.insertUser(user)
    }

    override suspend fun saveUsers(userList: List<User>): List<Long> = withContext(ioDispatcher) {
        usersDao.insertUsers(userList)
    }
}