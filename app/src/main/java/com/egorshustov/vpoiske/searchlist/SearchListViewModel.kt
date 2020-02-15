package com.egorshustov.vpoiske.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    val isLoading = MutableLiveData<Boolean>(true)

    val searchesWithUsers: LiveData<PagedList<SearchWithUsers>> =
        Transformations.map(
            searchesRepository.getSearchesWithUsers()
                .toLiveData(Config(pageSize = 10, enablePlaceholders = false, maxSize = 100))
        ) {
            isLoading.value = false
            it
        }

    fun openSearch(searchId: Long) {

    }

    /*val searchesWithExistingUsers: LiveData<List<SearchWithUsers>> =
        Transformations.map(searchesWithUsers) {
            it.filter { !it.userList.isNullOrEmpty() }
        }*/
}