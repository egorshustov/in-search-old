package com.egorshustov.vpoiske.searchlist

import androidx.fragment.app.viewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchListBinding

class SearchListFragment :
    BaseFragment<SearchListViewModel, FragmentSearchListBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_list

    override val viewModel by viewModels<SearchListViewModel> { viewModelFactory }
}