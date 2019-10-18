package com.egorshustov.vpoiske.di

import com.egorshustov.vpoiske.main.MainActivity
import com.egorshustov.vpoiske.main.MainViewPagerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun mainViewPagerFragment(): MainViewPagerFragment
}