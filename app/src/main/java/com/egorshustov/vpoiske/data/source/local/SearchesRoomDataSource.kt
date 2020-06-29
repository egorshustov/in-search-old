package com.egorshustov.vpoiske.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchesRoomDataSource @Inject constructor(
    private val searchesDao: SearchesDao,
    private val ioDispatcher: CoroutineDispatcher
) : SearchesLocalDataSource {

    override fun getSearchesWithUsers(): DataSource.Factory<Int, SearchWithUsers> =
        searchesDao.getSearchesWithUsers()

    override suspend fun getSearch(id: Long): Search? = withContext(ioDispatcher) {
        searchesDao.getSearch(id)
    }

    override fun getLastSearchId(): LiveData<Long?> = searchesDao.getLiveLastSearchId()

    override suspend fun deleteSearch(searchId: Long) = withContext(ioDispatcher) {
        searchesDao.deleteSearch(searchId)
    }

    override suspend fun saveSearchStartUnixSeconds(id: Long, startUnixSeconds: Int) =
        withContext(ioDispatcher) {
            searchesDao.updateSearchStartUnixSeconds(id, startUnixSeconds)
        }

    override suspend fun saveSearch(search: Search): Long = withContext(ioDispatcher) {
        searchesDao.insertSearch(search)
    }
}