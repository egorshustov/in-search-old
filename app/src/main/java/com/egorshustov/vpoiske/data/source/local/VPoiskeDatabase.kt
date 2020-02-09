package com.egorshustov.vpoiske.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.local.migrations.Migration1To2
import com.egorshustov.vpoiske.data.source.local.migrations.Migration2To3

@Database(
    entities = [User::class, Search::class, City::class, Country::class],
    version = 3,
    exportSchema = false
)
abstract class VPoiskeDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun searchesDao(): SearchesDao
    abstract fun citiesDao(): CitiesDao
    abstract fun countriesDao(): CountriesDao

    companion object {
        val MIGRATION_1_2 = Migration1To2()
        val MIGRATION_2_3 = Migration2To3()
    }
}