package com.egorshustov.vpoiske.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.UsersAdapter
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentMainBinding
import com.egorshustov.vpoiske.login.LoginViewModel
import com.egorshustov.vpoiske.searchprocessservice.SearchProcessService
import com.egorshustov.vpoiske.util.EventObserver
import com.egorshustov.vpoiske.util.safeNavigate
import com.egorshustov.vpoiske.util.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    //todo use or kotlin ext or data binding
    override fun getLayoutResId(): Int = R.layout.fragment_main

    override val viewModel: MainViewModel by activityViewModels()

    private val loginViewModel: LoginViewModel by activityViewModels()

    private var searchProcessService: SearchProcessService? = null

    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupUsersAdapter()
        setButtonListeners()
        observeSearchProcessBinder()
        observeAuthenticationState()
        observeOpenUserEvent()
        observeOpenNewSearch()
        observeMessage()
        observeCurrentSpanCountChanged()
    }

    override fun onResume() {
        super.onResume()
        binding.invalidateAll()
        startAndBindSearchProcessService()
    }

    private fun startAndBindSearchProcessService() {
        val serviceIntent = Intent(context, SearchProcessService::class.java)
        context?.let {
            ContextCompat.startForegroundService(it, serviceIntent)
            it.bindService(serviceIntent, viewModel.serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        unbindSearchProcessService()
        super.onPause()
    }

    private fun unbindSearchProcessService() {
        if (viewModel.searchProcessBinder.value != null) {
            context?.unbindService(viewModel.serviceConnection)
        }
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

    private fun setButtonListeners() = with(binding) {
        buttonStartNewSearch.setOnClickListener {
            findNavController().safeNavigate(
                MainFragmentDirections.actionMainFragmentToSearchParamsFragment()
            )
        }
    }

    private fun observeSearchProcessBinder() {
        viewModel.searchProcessBinder.observe(viewLifecycleOwner) {
            searchProcessService = it?.getService()
        }
    }

    private fun observeAuthenticationState() {
        loginViewModel.authenticationState.observe(viewLifecycleOwner) {
            when (it) {
                /*AuthenticationState.UNAUTHENTICATED -> findNavController().safeNavigate(
                    MainFragmentDirections.actionMainFragmentToLoginFragment()
                )*/
            }
        }
    }

    private fun observeOpenUserEvent() {
        viewModel.openUserEvent.observe(viewLifecycleOwner, EventObserver {
            openUserDetails(it)
        })
    }

    private fun observeOpenNewSearch() {
        viewModel.openNewSearch.observe(viewLifecycleOwner, EventObserver {
            findNavController().safeNavigate(
                MainFragmentDirections.actionMainFragmentToSearchParamsFragment()
            )
        })
    }

    private fun observeMessage() {
        viewModel.message.observe(viewLifecycleOwner,
            EventObserver { requireActivity().applicationContext.showMessage(it) })
    }

    private fun observeCurrentSpanCountChanged() {
        viewModel.currentSpanCountChanged.observe(viewLifecycleOwner) {
            gridLayoutManager.apply { spanCount = it }
        }
    }

    private fun openUserDetails(userId: Long) {
        /*val action = MainFragmentDirections.actionMainFragmentToUserDetailFragment(userId)
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
                /*findNavController().safeNavigate( //todo remove
                    MainFragmentDirections.actionMainFragmentToLoginFragment()
                )*/
                true
            }
            else -> false
        }
}
