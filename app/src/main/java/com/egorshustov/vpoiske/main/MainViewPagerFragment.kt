package com.egorshustov.vpoiske.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.adapters.MainViewPagerAdapter
import com.egorshustov.vpoiske.adapters.SEARCH_LOG_PAGE_INDEX
import com.egorshustov.vpoiske.adapters.USER_LIST_PAGE_INDEX
import com.egorshustov.vpoiske.databinding.FragmentMainViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment

class MainViewPagerFragment : DaggerFragment() {

    private lateinit var viewDataBinding: FragmentMainViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main_view_pager, container, false)
        viewDataBinding = FragmentMainViewPagerBinding.bind(root)
        val tabLayout = viewDataBinding.tabs
        val viewPager = viewDataBinding.viewPager

        viewPager.adapter = MainViewPagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
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
