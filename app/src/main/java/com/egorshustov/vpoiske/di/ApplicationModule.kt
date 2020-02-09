package com.egorshustov.vpoiske.di

import android.content.Context
import androidx.room.Room
import com.egorshustov.vpoiske.data.source.local.*
import com.egorshustov.vpoiske.data.source.remote.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideUsersRemoteDataSource(usersRetrofit: RetrofitVkApi): UsersRemoteDataSource {
        return UsersRetrofitDataSource(usersRetrofit)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCountriesRemoteDataSource(usersRetrofit: RetrofitVkApi): CountriesRemoteDataSource {
        return CountriesRetrofitDataSource(usersRetrofit)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCitiesRemoteDataSource(usersRetrofit: RetrofitVkApi): CitiesRemoteDataSource {
        return CitiesRetrofitDataSource(usersRetrofit)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideVkApiService(): RetrofitVkApi {
        return Retrofit.Builder()
            .baseUrl("https://api.vk.com/method/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitVkApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideUsersDao(vPoiskeDatabase: VPoiskeDatabase): UsersDao {
        return vPoiskeDatabase.usersDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSearchesDao(vPoiskeDatabase: VPoiskeDatabase): SearchesDao {
        return vPoiskeDatabase.searchesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCitiesDao(vPoiskeDatabase: VPoiskeDatabase): CitiesDao {
        return vPoiskeDatabase.citiesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideCountriesDao(vPoiskeDatabase: VPoiskeDatabase): CountriesDao {
        return vPoiskeDatabase.countriesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        VPoiskeDatabase::class.java,
        "VPoiske.db"
    ).addMigrations(VPoiskeDatabase.MIGRATION_1_2, VPoiskeDatabase.MIGRATION_2_3).build()

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

}
