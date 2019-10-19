package com.egorshustov.vpoiske.main


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.MainViewPagerAdapter
import com.egorshustov.vpoiske.adapters.SEARCH_LOG_PAGE_INDEX
import com.egorshustov.vpoiske.adapters.USER_LIST_PAGE_INDEX
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentMainViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainViewPagerFragment :
    BaseFragment<MainState, MainViewModel, FragmentMainViewPagerBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_main_view_pager

    override val viewModel by viewModels<MainViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = MainViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()
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
}
