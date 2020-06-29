package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import com.egorshustov.vpoiske.data.User

interface UsersLocalDataSource {

    fun getUsers(): LiveData<List<User>>

    suspend fun deleteUsersFromSearch(searchId: Long)

    suspend fun saveUser(user: User): Long

    suspend fun saveUsers(userList: List<User>): List<Long>
}