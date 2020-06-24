package com.egorshustov.vpoiske.di

import com.egorshustov.vpoiske.data.source.local.*
import com.egorshustov.vpoiske.data.source.remote.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class ApplicationModuleBinds {

    @Binds
    abstract fun bindUsersLocalDataSource(
        usersRoomDataSource: UsersRoomDataSource
    ): UsersLocalDataSource

    @Binds
    abstract fun bindUsersRemoteDataSource(
        usersRetrofitDataSource: UsersRetrofitDataSource
    ): UsersRemoteDataSource

    @Binds
    abstract fun bindCountriesLocalDataSource(
        countriesRoomDataSource: CountriesRoomDataSource
    ): CountriesLocalDataSource

    @Binds
    abstract fun bindCountriesRemoteDataSource(
        countriesRetrofitDataSource: CountriesRetrofitDataSource
    ): CountriesRemoteDataSource

    @Binds
    abstract fun bindCitiesRemoteDataSource(
        citiesRetrofitDataSource: CitiesRetrofitDataSource
    ): CitiesRemoteDataSource

    @Binds
    abstract fun bindSearchesLocalDataSource(
        searchesLocalDataSource: SearchesLocalDataSource
    ): SearchesLocalDataSource
}