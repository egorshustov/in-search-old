package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.searchlog.SearchLogFragment
import com.egorshustov.vpoiske.searchlog.SearchLogViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchLogModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun searchLogFragment(): SearchLogFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchLogViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchLogViewModel): ViewModel
}