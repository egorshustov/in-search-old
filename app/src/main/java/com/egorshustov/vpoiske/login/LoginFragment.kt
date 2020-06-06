package com.egorshustov.vpoiske.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentSearchParamsBinding
import com.egorshustov.vpoiske.main.MainViewModel

class LoginFragment : BaseFragment<LoginViewModel, FragmentSearchParamsBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override val viewModel by activityViewModels<LoginViewModel> { viewModelFactory }

    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}