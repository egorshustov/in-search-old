package com.egorshustov.vpoiske.di

import com.egorshustov.vpoiske.main.UserListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserListModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun userListFragment(): UserListFragment
}