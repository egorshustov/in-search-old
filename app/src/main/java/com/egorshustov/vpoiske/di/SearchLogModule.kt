package com.egorshustov.vpoiske.di

import com.egorshustov.vpoiske.main.SearchLogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchLogModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun searchLogFragment(): SearchLogFragment
}