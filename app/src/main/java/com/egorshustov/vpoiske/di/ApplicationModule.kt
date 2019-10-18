package com.egorshustov.vpoiske.di

import android.content.Context
import androidx.room.Room
import com.egorshustov.vpoiske.data.source.local.VPoiskeDatabase
import com.egorshustov.vpoiske.data.source.remote.VkApiService
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
    fun provideVkApiService(): VkApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.vk.com/method/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VkApiService::class.java)
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
