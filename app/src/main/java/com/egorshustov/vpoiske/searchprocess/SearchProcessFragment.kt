package com.egorshustov.vpoiske.searchprocess

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.UsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchProcessBinding
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.showMessage

class SearchProcessFragment : BaseFragment<SearchProcessViewModel, FragmentSearchProcessBinding>() {

    //todo use or kotlin ext or data binding
    override fun getLayoutResId(): Int = R.layout.fragment_search_process

    override val viewModel by activityViewModels<SearchProcessViewModel> { viewModelFactory }

    private lateinit var gridLayoutManager: GridLayoutManager
    private var spanCount = DEFAULT_SPAN_COUNT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupUsersAdapter()
        observeOpenUserEvent()
        observeOpenNewSearch()
        observeMessage()
    }

    private fun setupUsersAdapter() {
        gridLayoutManager =
            GridLayoutManager(requireContext(), spanCount, RecyclerView.VERTICAL, false)
        binding.recyclerUsers.apply {
            layoutManager = gridLayoutManager
            adapter = UsersAdapter(viewModel)
        }
    }

    private fun observeOpenUserEvent() {
        viewModel.openUserEvent.observe(viewLifecycleOwner, EventObserver {
            openUserDetails(it)
        })
    }

    private fun observeOpenNewSearch() {
        viewModel.openNewSearch.observe(viewLifecycleOwner, EventObserver {
            val action =
                SearchProcessFragmentDirections.actionSearchProcessFragmentToSearchParamsFragment()
            findNavController().navigate(action)
        })
    }

    private fun observeMessage() {
        viewModel.message.observe(
            viewLifecycleOwner,
            EventObserver { requireActivity().applicationContext.showMessage(it) })
    }

    private fun openUserDetails(userId: Long) {
        /*val action = SearchProcessFragmentDirections.actionSearchProcessFragmentToUserDetailFragment(userId)
        findNavController().navigate(action)*/
        val userUrl = "https://vk.com/id$userId"
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(userUrl) }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.item_change_view -> {
                gridLayoutManager.apply {
                    spanCount = if (spanCount.dec() == 0) MAX_SPAN_COUNT else spanCount.dec()
                    this@SearchProcessFragment.spanCount = spanCount
                }
                true
            }
            else -> false
        }

    companion object {
        private const val DEFAULT_SPAN_COUNT = 3
        private const val MAX_SPAN_COUNT = 3
    }
}
