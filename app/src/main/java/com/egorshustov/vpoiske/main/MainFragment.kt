package com.egorshustov.vpoiske.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
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
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    //todo use or kotlin ext or data binding
    override fun getLayoutResId(): Int = R.layout.fragment_main

    override val viewModel: MainViewModel by activityViewModels()

    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var gridLayoutManager: GridLayoutManager

    private lateinit var searchProcessService: SearchProcessService

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder) {
            Timber.d("ServiceConnection: onServiceConnected")
            searchProcessService =
                (iBinder as SearchProcessService.SearchProcessBinder).getService()
            observeSearchProcessLiveData()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.d("ServiceConnection: onServiceDisconnected")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupUsersAdapter()
        setButtonListeners()
        observeAuthenticationState()
        observeOpenUserEvent()
        observeSearchParams()
        observeSearchProcessCommands()
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
            it.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        context?.unbindService(serviceConnection)
        super.onPause()
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
        viewModel.openUserDetails.observe(viewLifecycleOwner, EventObserver {
            openUserDetails(it)
        })
    }

    private fun observeSearchParams() {
        viewModel.openSearchParams.observe(viewLifecycleOwner, EventObserver {
            findNavController().safeNavigate(
                MainFragmentDirections.actionMainFragmentToSearchParamsFragment()
            )
        })
    }

    private fun observeSearchProcessCommands() = with(viewModel) {
        startNewSearch.observe(viewLifecycleOwner, EventObserver {
            searchProcessService.startSearch(it)
        })
        stopSearch.observe(viewLifecycleOwner, EventObserver {
            searchProcessService.stopSearch()
        })
    }

    private fun observeSearchProcessLiveData() {
        searchProcessService.isSearchRunning.observe(viewLifecycleOwner) {
            context?.showMessage("isSearchRunning: $it")
            viewModel.searchState.value =
                if (it) SearchProcessState.IN_PROGRESS else SearchProcessState.INACTIVE
        }
    }

    private fun observeMessage() {
        viewModel.message.observe(
            viewLifecycleOwner,
            EventObserver { requireActivity().showMessage(it) })
    }

    private fun observeCurrentSpanCountChanged() {
        viewModel.currentSpanCountChanged.observe(viewLifecycleOwner) {
            gridLayoutManager.apply { spanCount = it }
        }
    }

    private fun openUserDetails(userId: Long) {
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
