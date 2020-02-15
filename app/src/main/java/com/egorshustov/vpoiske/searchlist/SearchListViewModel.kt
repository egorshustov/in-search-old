package com.egorshustov.vpoiske.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class SearchListViewModel @Inject constructor(
    private val searchesRepository: SearchesRepository
) : ViewModel() {

    val searchesWithUsers: LiveData<PagedList<SearchWithUsers>> =
        searchesRepository.getSearchesWithUsers()
            .toLiveData(Config(pageSize = 10, enablePlaceholders = false, maxSize = 100))

    fun openSearch(searchId: Long) {

    }

    /*val searchesWithExistingUsers: LiveData<List<SearchWithUsers>> =
        Transformations.map(searchesWithUsers) {
            it.filter { !it.userList.isNullOrEmpty() }
        }*/
}