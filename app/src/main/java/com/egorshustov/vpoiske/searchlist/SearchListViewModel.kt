package com.egorshustov.vpoiske.searchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.source.SearchesRepository
import javax.inject.Inject

class SearchListViewModel @Inject constructor(
    private val searchesRepository: SearchesRepository
) : ViewModel() {
    //todo filter searches with transformations to show only searches with users
    val searchesWithUsers: LiveData<List<SearchWithUsers>> =
        searchesRepository.getLiveSearchesWithUsers()
}