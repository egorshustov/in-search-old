package com.egorshustov.vpoiske.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.egorshustov.vpoiske.main.SearchLogFragment
import com.egorshustov.vpoiske.main.UserListFragment

const val USER_LIST_PAGE_INDEX = 0
const val SEARCH_LOG_PAGE_INDEX = 1

class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        USER_LIST_PAGE_INDEX to { UserListFragment() },
        SEARCH_LOG_PAGE_INDEX to { SearchLogFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}