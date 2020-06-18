package com.egorshustov.vpoiske.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.BR

abstract class BaseFragment<VM : ViewModel, Binding : ViewDataBinding> : Fragment() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    protected abstract val viewModel: ViewModel

    protected lateinit var binding: Binding
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DataBindingUtil.inflate<Binding>(inflater, getLayoutResId(), container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                setVariable(BR.viewmodel, viewModel)
                binding = this
            }.root
    }
}
