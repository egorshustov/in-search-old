package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers

interface SearchesRepository {

    fun getSearchesWithUsers(): DataSource.Factory<Int, SearchWithUsers>

    suspend fun getSearch(id: Long): Search?

    fun getLastSearchId(): LiveData<Long?>

    suspend fun saveSearchStartUnixSeconds(id: Long, startUnixSeconds: Int)

    suspend fun saveSearch(search: Search): Long
}