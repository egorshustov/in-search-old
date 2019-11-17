package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.egorshustov.vpoiske.data.User

@Dao
interface UsersDao {
    @Query("select * from users")
    fun getLiveUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(userList: List<User>): List<Long>
}