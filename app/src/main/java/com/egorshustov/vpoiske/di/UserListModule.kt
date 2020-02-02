package com.egorshustov.vpoiske.di

import androidx.lifecycle.ViewModel
import com.egorshustov.vpoiske.userlist.UserListFragment
import com.egorshustov.vpoiske.userlist.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class UserListModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun userListFragment(): UserListFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    abstract fun bindViewModel(viewmodel: UserListViewModel): ViewModel
}