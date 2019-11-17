package com.egorshustov.vpoiske.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.MainViewPagerAdapter
import com.egorshustov.vpoiske.adapters.SEARCH_LOG_PAGE_INDEX
import com.egorshustov.vpoiske.adapters.USER_LIST_PAGE_INDEX
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentMainViewPagerBinding
import com.egorshustov.vpoiske.util.EventObserver
import com.google.android.material.tabs.TabLayoutMediator

class MainViewPagerFragment :
    BaseFragment<MainState, MainViewModel, FragmentMainViewPagerBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_main_view_pager

    override val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayoutMain = binding.tabLayoutMain
        val viewPagerMain = binding.viewPagerMain

        viewPagerMain.adapter = MainViewPagerAdapter(this)
        TabLayoutMediator(tabLayoutMain, viewPagerMain) { tab, position ->
            //tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()
        observeOpenNewSearch()
    }

    /*private fun getTabIcon(position: Int): Int {
        return when (position) {
            USER_LIST_PAGE_INDEX -> R.drawable.user_list_tab_selector
            SEARCH_LOG_PAGE_INDEX -> R.drawable.search_log_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }*/

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            USER_LIST_PAGE_INDEX -> getString(R.string.user_list_title)
            SEARCH_LOG_PAGE_INDEX -> getString(R.string.search_log_title)
            else -> null
        }
    }

    private fun observeOpenNewSearch() {
        viewModel.openNewSearch.observe(viewLifecycleOwner, EventObserver {
            val action =
                MainViewPagerFragmentDirections.actionMainViewPagerFragmentToNewSearchFragment()
            findNavController().navigate(action)
        })
    }
}
