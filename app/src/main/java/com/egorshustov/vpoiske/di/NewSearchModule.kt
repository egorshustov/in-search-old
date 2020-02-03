package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.searchparams.SearchParamsFragment
import com.egorshustov.vpoiske.searchparams.SearchParamsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class NewSearchModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun newSearchFragment(): SearchParamsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchParamsViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchParamsViewModel): ViewModel
}