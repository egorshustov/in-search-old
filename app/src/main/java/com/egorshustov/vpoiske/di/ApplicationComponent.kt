package com.egorshustov.vpoiske.di

import android.content.Context
import com.egorshustov.vpoiske.VPoiskeApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidInjectionModule::class,
        VPoiskeModule::class,
        SearchProcessModule::class,
        SearchParamsModule::class,
        SearchListModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<VPoiskeApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}