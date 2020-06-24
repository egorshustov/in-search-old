package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.source.local.SearchesLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchesRepository @Inject constructor(
    private val searchesLocalDataSource: SearchesLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getSearchesWithUsers(): DataSource.Factory<Int, SearchWithUsers> =
        searchesLocalDataSource.getSearchesWithUsers()

    suspend fun getSearch(id: Long): Search? =
        withContext(ioDispatcher) { searchesLocalDataSource.getSearch(id) }

    fun getLastSearchId(): LiveData<Long?> = searchesLocalDataSource.getLastSearchId()

    suspend fun saveSearchStartUnixSeconds(id: Long, startUnixSeconds: Int) =
        withContext(ioDispatcher) {
            searchesLocalDataSource.saveSearchStartUnixSeconds(id, startUnixSeconds)
        }

    suspend fun saveSearch(search: Search): Long =
        withContext(ioDispatcher) { searchesLocalDataSource.saveSearch(search) }
}