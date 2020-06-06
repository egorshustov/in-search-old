package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.main.MainFragment
import com.egorshustov.vpoiske.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun mainFragment(): MainFragment

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}