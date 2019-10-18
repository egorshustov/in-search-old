package com.egorshustov.vpoiske.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.egorshustov.vpoiske.data.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class VPoiskeDatabase : RoomDatabase() {

    abstract fun userDao(): UsersDao
}
