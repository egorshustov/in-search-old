package com.egorshustov.vpoiske.di

import com.egorshustov.vpoiske.VPoiskeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VPoiskeModule {
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun contributeMainActivity(): VPoiskeActivity
}