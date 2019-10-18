package com.egorshustov.vpoiske.newsearch


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.databinding.FragmentNewSearchBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class NewSearchFragment : DaggerFragment() {

    private lateinit var viewDataBinding: FragmentNewSearchBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<NewSearchViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_new_search, container, false)
        viewDataBinding = FragmentNewSearchBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }


}
