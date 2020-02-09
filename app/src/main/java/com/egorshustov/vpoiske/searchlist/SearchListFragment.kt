package com.egorshustov.vpoiske.searchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.SearchWithUsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchListBinding

class SearchListFragment :
    BaseFragment<SearchListViewModel, FragmentSearchListBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_list

    override val viewModel by viewModels<SearchListViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchWithUsersAdapter()
    }

    private fun setupSearchWithUsersAdapter() {
        binding.recyclerSearches.adapter = SearchWithUsersAdapter(viewModel)
    }
}