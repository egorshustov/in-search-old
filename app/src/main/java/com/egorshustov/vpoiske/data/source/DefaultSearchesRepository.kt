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
class DefaultSearchesRepository @Inject constructor(
    private val searchesLocalDataSource: SearchesLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : SearchesRepository {

    override fun getSearchesWithUsers(): DataSource.Factory<Int, SearchWithUsers> =
        searchesLocalDataSource.getSearchesWithUsers()

    override suspend fun getSearch(id: Long): Search? =
        withContext(ioDispatcher) { searchesLocalDataSource.getSearch(id) }

    override fun getLastSearchId(): LiveData<Long?> = searchesLocalDataSource.getLastSearchId()

    override suspend fun deleteSearch(id: Long) =
        withContext(ioDispatcher) { searchesLocalDataSource.deleteSearch(id) }

    override suspend fun saveSearchStartUnixSeconds(id: Long, startUnixSeconds: Int) =
        withContext(ioDispatcher) {
            searchesLocalDataSource.saveSearchStartUnixSeconds(id, startUnixSeconds)
        }

    override suspend fun saveSearch(search: Search): Long =
        withContext(ioDispatcher) { searchesLocalDataSource.saveSearch(search) }
}