package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers

@Dao
interface SearchesDao {

    @Transaction
    @Query("select * from searches order by start_unix_seconds desc")
    fun getSearchesWithUsers(): DataSource.Factory<Int, SearchWithUsers>

    @Query("select * from searches where id = :id")
    suspend fun getSearch(id: Long): Search?

    @Query("select id from searches order by start_unix_seconds desc")
    fun getLiveLastSearchId(): LiveData<Long?>

    @Query("delete from searches where id = :id")
    suspend fun deleteSearch(id: Long)

    @Query("update searches set start_unix_seconds = :startUnixSeconds where id = :id and start_unix_seconds <> :startUnixSeconds")
    suspend fun updateSearchStartUnixSeconds(id: Long, startUnixSeconds: Int)

    @Insert
    suspend fun insertSearch(search: Search): Long
}