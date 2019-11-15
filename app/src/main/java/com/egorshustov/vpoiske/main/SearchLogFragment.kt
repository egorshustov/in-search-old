package com.egorshustov.vpoiske.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchLogBinding
import com.egorshustov.vpoiske.util.EventObserver
import kotlinx.android.synthetic.main.fragment_search_log.*

class SearchLogFragment :
    BaseFragment<MainState, MainViewModel, FragmentSearchLogBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_search_log

    override val viewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveLogMessage()
    }

    private fun observeLiveLogMessage() {
        viewModel.logMessage.observe(viewLifecycleOwner, EventObserver {
            text_search_log.append("$it\n")
        })
    }
}