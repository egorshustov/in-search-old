package com.egorshustov.vpoiske.newsearch


import androidx.fragment.app.viewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentNewSearchBinding

class NewSearchFragment :
    BaseFragment<NewSearchState, NewSearchViewModel, FragmentNewSearchBinding>() {
    override fun getLayoutResId(): Int = R.layout.fragment_new_search

    override val viewModel by viewModels<NewSearchViewModel> { viewModelFactory }
}
