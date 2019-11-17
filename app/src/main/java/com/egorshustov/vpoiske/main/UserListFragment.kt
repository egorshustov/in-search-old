package com.egorshustov.vpoiske.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.UsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentUserListBinding
import com.egorshustov.vpoiske.util.EventObserver

class UserListFragment : BaseFragment<MainState, MainViewModel, FragmentUserListBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_user_list

    override val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        observeOpenUserEvent()
    }

    private fun setupListAdapter() {
        binding.recyclerUsers.adapter = UsersAdapter(viewModel)
    }

    private fun observeOpenUserEvent() {
        viewModel.openUserEvent.observe(viewLifecycleOwner, EventObserver {
            openUserDetails(it)
        })
    }

    private fun openUserDetails(userId: Long) {
        /*val action = MainViewPagerFragmentDirections.actionMainViewPagerFragmentToUserDetailFragment(userId)
        findNavController().navigate(action)*/
        val userUrl = "https://vk.com/id$userId"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(userUrl)
        }
        startActivity(intent)
    }

}
