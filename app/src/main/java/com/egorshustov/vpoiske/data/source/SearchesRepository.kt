package com.egorshustov.vpoiske.data.source

import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.source.local.SearchesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchesRepository @Inject constructor(
    private val searchesDao: SearchesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun insertSearch(search: Search): Long =
        withContext(ioDispatcher) { searchesDao.insertSearch(search) }

    suspend fun getSearch(id: Long): Search? =
        withContext(ioDispatcher) { searchesDao.getSearch(id) }

    suspend fun updateSearchStartUnixSeconds(id: Long, startUnixSeconds: Int) =
        withContext(ioDispatcher) { searchesDao.updateSearchStartUnixSeconds(id, startUnixSeconds) }

    fun getLiveSearchesWithUsers() = searchesDao.getLiveSearchesWithUsers()
}