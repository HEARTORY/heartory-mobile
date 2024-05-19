package com.heartsteel.heartory.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireBaseDataBaseRef(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Provides
    @Singleton
    fun provideFireBaseDataBase(): FirebaseDatabase = FirebaseDatabase.getInstance()

}