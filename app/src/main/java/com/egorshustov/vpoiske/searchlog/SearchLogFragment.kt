package com.egorshustov.vpoiske.searchlog


import androidx.fragment.app.viewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchLogBinding

class SearchLogFragment :
    BaseFragment<SearchLogState, SearchLogViewModel, FragmentSearchLogBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_log

    override val viewModel by viewModels<SearchLogViewModel> { viewModelFactory }
}