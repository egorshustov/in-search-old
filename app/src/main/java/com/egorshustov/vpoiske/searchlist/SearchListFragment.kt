package com.egorshustov.vpoiske.searchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.SearchWithUsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchListBinding
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchListFragment : BaseFragment<SearchListViewModel, FragmentSearchListBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_list

    override val viewModel: SearchListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchWithUsersAdapter()
        observeOpenSearch()
    }

    private fun setupSearchWithUsersAdapter() {
        binding.recyclerSearches.adapter = SearchWithUsersAdapter(viewModel)
    }

    private fun observeOpenSearch() {
        viewModel.openSearch.observe(viewLifecycleOwner, EventObserver {
            val action =
                SearchListFragmentDirections.actionSearchListFragmentToSearchFragment(it)
            findNavController().safeNavigate(action)
        })
    }
}