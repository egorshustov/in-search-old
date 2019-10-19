package com.egorshustov.vpoiske.userlist


import androidx.fragment.app.viewModels
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.base.BaseFragment
import com.egorshustov.vpoiske.databinding.FragmentUserListBinding

class UserListFragment : BaseFragment<UserListState, UserListViewModel, FragmentUserListBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_user_list

    override val viewModel by viewModels<UserListViewModel> { viewModelFactory }
}
