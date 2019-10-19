package com.egorshustov.vpoiske.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.egorshustov.vpoiske.BR
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<State : BaseState, ViewModel : BaseViewModel<State>, Binding : ViewDataBinding> :
    DaggerFragment() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModel: ViewModel

    protected lateinit var binding: Binding
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<Binding>(inflater, getLayoutResId(), container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewmodel, viewModel)
            binding = this
        }.root
    }
}
