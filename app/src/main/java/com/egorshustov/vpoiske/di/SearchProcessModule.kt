package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.searchprocess.SearchProcessFragment
import com.egorshustov.vpoiske.searchprocess.SearchProcessViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchProcessModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun searchProcessFragment(): SearchProcessFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchProcessViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchProcessViewModel): ViewModel
}