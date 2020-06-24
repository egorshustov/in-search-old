package com.egorshustov.vpoiske.searchlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.source.SearchesRepository
import com.egorshustov.vpoiske.util.Event

class SearchListViewModel @ViewModelInject constructor(searchesRepository: SearchesRepository) :
    ViewModel() {

    private val _openSearch = MutableLiveData<Event<Long>>()
    val openSearch: LiveData<Event<Long>> = _openSearch

    val isLoading = MutableLiveData(true)

    val searchesWithUsers: LiveData<PagedList<SearchWithUsers>> =
        searchesRepository.getSearchesWithUsers()
            .toLiveData(Config(pageSize = 10, enablePlaceholders = false, maxSize = 100)).map {
                isLoading.value = false
                it
            }

    fun openSearch(searchId: Long) {
        _openSearch.value = Event(searchId)
    }
}