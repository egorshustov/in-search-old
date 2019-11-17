package com.egorshustov.vpoiske.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.User

@Database(
    entities = [User::class, Search::class, City::class, Country::class],
    version = 1,
    exportSchema = false
)
abstract class VPoiskeDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun searchesDao(): SearchesDao
    abstract fun citiesDao(): CitiesDao
    abstract fun countriesDao(): CountriesDao
}