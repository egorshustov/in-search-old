package com.egorshustov.vpoiske.newsearch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentNewSearchBinding

class NewSearchFragment :
    BaseFragment<NewSearchState, NewSearchViewModel, FragmentNewSearchBinding>() {
    override fun getLayoutResId(): Int = R.layout.fragment_new_search

    override val viewModel by viewModels<NewSearchViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveCountries()

    }

    private fun observeLiveCountries() {
        viewModel.liveCountries.observe(viewLifecycleOwner, Observer {
            val countryList = it
        })
    }
}
