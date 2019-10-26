package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.egorshustov.vpoiske.data.Country

@Dao
interface CountriesDao {
    @Query("select * from countries")
    fun getLiveCountries(): LiveData<List<Country>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCountries(groupList: List<Country>)
}