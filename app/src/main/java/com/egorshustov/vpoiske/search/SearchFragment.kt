package com.egorshustov.vpoiske.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.UsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchBinding
import com.egorshustov.vpoiske.util.EventObserver

class SearchFragment :
    BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    private val args: SearchFragmentArgs by navArgs()

    override fun getLayoutResId(): Int = R.layout.fragment_search

    override val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupUsersAdapter()
        observeOpenUserEvent()
        observeCurrentSpanCountChanged()
        viewModel.onCurrentSearchIdObtained(args.searchId)
    }

    private fun setupUsersAdapter() {
        gridLayoutManager =
            GridLayoutManager(
                requireContext(),
                viewModel.currentSpanCount,
                RecyclerView.VERTICAL,
                false
            )
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

    private fun observeCurrentSpanCountChanged() {
        viewModel.currentSpanCountChanged.observe(viewLifecycleOwner) {
            gridLayoutManager.apply { spanCount = it }
        }
    }

    private fun openUserDetails(userId: Long) {
        /*val action = SearchProcessFragmentDirections.actionSearchProcessFragmentToUserDetailFragment(userId)
        findNavController().safeNavigate(action)*/
        val userUrl = "https://vk.com/id$userId"
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(userUrl) }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.item_change_view -> {
                viewModel.onItemChangeViewClicked()
                true
            }
            else -> false
        }
}