package com.egorshustov.vpoiske.searchlog

import androidx.fragment.app.activityViewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchLogBinding
import com.egorshustov.vpoiske.main.MainState
import com.egorshustov.vpoiske.main.MainViewModel

class SearchLogFragment :
    BaseFragment<MainState, MainViewModel, FragmentSearchLogBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_log

    override val viewModel by activityViewModels<MainViewModel> { viewModelFactory }
}