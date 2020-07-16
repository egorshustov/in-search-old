package com.egorshustov.vpoiske.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.DataSource
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers
import java.util.*
import javax.inject.Inject

class FakeSearchesRepository @Inject constructor() : SearchesRepository {

    private var searchesMap: LinkedHashMap<Long, Search> = LinkedHashMap()

    override fun getSearchesWithUsers(): DataSource.Factory<Int, SearchWithUsers> {
        throw NotImplementedError()
    }

    override suspend fun getSearch(id: Long): Search? = searchesMap.getOrDefault(id, null)

    override fun getLastSearchId(): LiveData<Long?> = liveData {
        emit(searchesMap.values.maxBy { it.startUnixSeconds }?.id)
    }

    override suspend fun deleteSearch(id: Long) {
        searchesMap.remove(id)
    }

    override suspend fun saveSearchStartUnixSeconds(id: Long, startUnixSeconds: Int) {
        throw NotImplementedError()
    }

    override suspend fun saveSearch(search: Search): Long {
        val prevSearch = searchesMap.put(search.id, search)
        return if (prevSearch == null) {
            1
        } else {
            0
        }
    }
}