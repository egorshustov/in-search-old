package com.egorshustov.vpoiske.userlist

import androidx.fragment.app.activityViewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentUserListBinding
import com.egorshustov.vpoiske.main.MainState
import com.egorshustov.vpoiske.main.MainViewModel

class UserListFragment : BaseFragment<MainState, MainViewModel, FragmentUserListBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_user_list

    override val viewModel by activityViewModels<MainViewModel> { viewModelFactory }
}
