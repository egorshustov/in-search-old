package com.egorshustov.vpoiske.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchWithUsersAdapter()
        observeOpenUserEvent()
        viewModel.onCurrentSearchIdObtained(args.searchId)
    }

    private fun setupSearchWithUsersAdapter() {
        binding.recyclerUsers.adapter = UsersAdapter(viewModel)
    }

    private fun observeOpenUserEvent() {
        viewModel.openUserEvent.observe(viewLifecycleOwner, EventObserver {
            openUserDetails(it)
        })
    }

    private fun openUserDetails(userId: Long) {
        /*val action = SearchProcessFragmentDirections.actionSearchProcessFragmentToUserDetailFragment(userId)
        findNavController().navigate(action)*/
        val userUrl = "https://vk.com/id$userId"
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(userUrl) }
        startActivity(intent)
    }
}