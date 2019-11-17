package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.egorshustov.vpoiske.data.Search

@Dao
interface SearchesDao {
    @Query("select * from searches")
    fun getLiveSearches(): LiveData<List<Search>>

    @Insert
    suspend fun insertSearch(search: Search): Long
}