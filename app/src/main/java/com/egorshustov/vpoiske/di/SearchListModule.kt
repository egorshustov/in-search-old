package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.searchlist.SearchListFragment
import com.egorshustov.vpoiske.searchlist.SearchListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchListModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun searchListFragment(): SearchListFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchListViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchListViewModel): ViewModel
}