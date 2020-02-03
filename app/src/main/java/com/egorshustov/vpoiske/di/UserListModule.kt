package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.search.SearchFragment
import com.egorshustov.vpoiske.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class UserListModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun userListFragment(): SearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchViewModel): ViewModel
}