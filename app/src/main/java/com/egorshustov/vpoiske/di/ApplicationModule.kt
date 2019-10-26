package com.egorshustov.vpoiske.di

import android.content.Context
import androidx.room.Room
import com.egorshustov.vpoiske.data.source.local.CitiesDao
import com.egorshustov.vpoiske.data.source.local.CountriesDao
import com.egorshustov.vpoiske.data.source.local.UsersDao
import com.egorshustov.vpoiske.data.source.local.VPoiskeDatabase
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
    fun provideDatabase(context: Context): VPoiskeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            VPoiskeDatabase::class.java,
            "VPoiske.db"
        ).build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

}
