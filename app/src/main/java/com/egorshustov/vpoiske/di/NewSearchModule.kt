package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.newsearch.NewSearchFragment
import com.egorshustov.vpoiske.newsearch.NewSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class NewSearchModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun newSearchFragment(): NewSearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(NewSearchViewModel::class)
    abstract fun bindViewModel(viewmodel: NewSearchViewModel): ViewModel
}