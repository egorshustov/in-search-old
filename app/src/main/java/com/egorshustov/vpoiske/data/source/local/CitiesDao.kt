package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.egorshustov.vpoiske.data.City

@Dao
interface CitiesDao {
    @Query("select * from cities")
    fun getLiveCities(): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCities(cityList: List<City>)
}