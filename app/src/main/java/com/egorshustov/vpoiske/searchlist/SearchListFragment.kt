package com.egorshustov.vpoiske.searchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.SearchWithUsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchListBinding
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchListFragment : BaseFragment<SearchListViewModel, FragmentSearchListBinding>(),
    SearchItemTouchHelper.SearchItemTouchHelperListener {

    override fun getLayoutResId(): Int = R.layout.fragment_search_list

    override val viewModel: SearchListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerSearches()
        observeOpenSearch()
    }

    private fun setupRecyclerSearches() = with(binding) {
        val searchItemTouchHelper =
            SearchItemTouchHelper(0, ItemTouchHelper.LEFT, this@SearchListFragment)
        ItemTouchHelper(searchItemTouchHelper).attachToRecyclerView(recyclerSearches)
        recyclerSearches.adapter = SearchWithUsersAdapter(viewModel)
    }

    private fun observeOpenSearch() {
        viewModel.openSearch.observe(viewLifecycleOwner, EventObserver {
            val action =
                SearchListFragmentDirections.actionSearchListFragmentToSearchFragment(it)
            findNavController().safeNavigate(action)
        })
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val pos = viewHolder.adapterPosition
    }
}