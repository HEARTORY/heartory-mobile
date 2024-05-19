package com.heartsteel.heartory.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.heartsteel.heartory.data.sharePreference.AppSharePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(AppSharePreference.APP_SHARE_KEY, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppSharePreference(@ApplicationContext context: Context): AppSharePreference {
        return AppSharePreference(context)
    }


}