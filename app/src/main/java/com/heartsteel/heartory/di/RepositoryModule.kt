package com.heartsteel.heartory.di

import com.google.firebase.auth.FirebaseAuth
import com.heartsteel.heartory.data.repository.AuthRepository
import com.heartsteel.heartory.data.sharePreference.AppSharePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, appSharePreference: AppSharePreference): AuthRepository {
        return AuthRepository(firebaseAuth, appSharePreference)
    }

}