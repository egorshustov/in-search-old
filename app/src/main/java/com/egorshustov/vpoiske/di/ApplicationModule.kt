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
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideUsersRemoteDataSource(usersRetrofit: RetrofitVkApi): UsersRemoteDataSource =
        UsersRetrofitDataSource(usersRetrofit)

    @JvmStatic
    @Singleton
    @Provides
    fun provideCountriesRemoteDataSource(usersRetrofit: RetrofitVkApi): CountriesRemoteDataSource =
        CountriesRetrofitDataSource(usersRetrofit)

    @JvmStatic
    @Singleton
    @Provides
    fun provideCitiesRemoteDataSource(usersRetrofit: RetrofitVkApi): CitiesRemoteDataSource =
        CitiesRetrofitDataSource(usersRetrofit)

    @JvmStatic
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @JvmStatic
    @Singleton
    @Provides
    fun provideVkApiService(okHttpClient: OkHttpClient): RetrofitVkApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.vk.com/method/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitVkApi::class.java)

    @JvmStatic
    @Singleton
    @Provides
    fun provideUsersDao(vPoiskeDatabase: VPoiskeDatabase): UsersDao = vPoiskeDatabase.usersDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideSearchesDao(vPoiskeDatabase: VPoiskeDatabase): SearchesDao =
        vPoiskeDatabase.searchesDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideCitiesDao(vPoiskeDatabase: VPoiskeDatabase): CitiesDao = vPoiskeDatabase.citiesDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideCountriesDao(vPoiskeDatabase: VPoiskeDatabase): CountriesDao =
        vPoiskeDatabase.countriesDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(context: Context): VPoiskeDatabase = Room.databaseBuilder(
        context.applicationContext,
        VPoiskeDatabase::class.java,
        "VPoiske.db"
    ).addMigrations(VPoiskeDatabase.MIGRATION_1_2, VPoiskeDatabase.MIGRATION_2_3).build()

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            V_POISKE_PREFERENCES_FILENAME, Context.MODE_PRIVATE
        )
}

@Module
abstract class ApplicationModuleBinds