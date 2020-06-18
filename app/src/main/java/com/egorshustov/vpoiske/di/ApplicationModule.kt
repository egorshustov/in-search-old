package com.egorshustov.vpoiske.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.egorshustov.vpoiske.BuildConfig
import com.egorshustov.vpoiske.data.source.local.*
import com.egorshustov.vpoiske.data.source.remote.*
import com.egorshustov.vpoiske.util.V_POISKE_PREFERENCES_FILENAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideUsersRemoteDataSource(usersRetrofit: RetrofitVkApi): UsersRemoteDataSource =
        UsersRetrofitDataSource(usersRetrofit)

    @Singleton
    @Provides
    fun provideCountriesRemoteDataSource(usersRetrofit: RetrofitVkApi): CountriesRemoteDataSource =
        CountriesRetrofitDataSource(usersRetrofit)

    @Singleton
    @Provides
    fun provideCitiesRemoteDataSource(usersRetrofit: RetrofitVkApi): CitiesRemoteDataSource =
        CitiesRetrofitDataSource(usersRetrofit)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideVkApiService(okHttpClient: OkHttpClient): RetrofitVkApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.vk.com/method/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitVkApi::class.java)

    @Singleton
    @Provides
    fun provideUsersDao(vPoiskeDatabase: VPoiskeDatabase): UsersDao = vPoiskeDatabase.usersDao()

    @Singleton
    @Provides
    fun provideSearchesDao(vPoiskeDatabase: VPoiskeDatabase): SearchesDao =
        vPoiskeDatabase.searchesDao()

    @Singleton
    @Provides
    fun provideCitiesDao(vPoiskeDatabase: VPoiskeDatabase): CitiesDao = vPoiskeDatabase.citiesDao()

    @Singleton
    @Provides
    fun provideCountriesDao(vPoiskeDatabase: VPoiskeDatabase): CountriesDao =
        vPoiskeDatabase.countriesDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): VPoiskeDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            VPoiskeDatabase::class.java,
            "VPoiske.db"
        ).addMigrations(VPoiskeDatabase.MIGRATION_1_2, VPoiskeDatabase.MIGRATION_2_3).build()

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            V_POISKE_PREFERENCES_FILENAME, Context.MODE_PRIVATE
        )
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class ApplicationModuleBinds