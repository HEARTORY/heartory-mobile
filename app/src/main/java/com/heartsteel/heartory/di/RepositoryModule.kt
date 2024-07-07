package com.heartsteel.heartory.di

import com.google.firebase.auth.FirebaseAuth
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.repository.UserRepository
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        appSharePreference: AppSharePreference,
        privateRetrofit: PrivateRetrofit,
        publicRetrofit: PublicRetrofit,
        jwtRepository: JwtRepository
    ): UserRepository {
        return UserRepository(firebaseAuth, appSharePreference, privateRetrofit, publicRetrofit, jwtRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideJwtRepository(
        appSharePreference: AppSharePreference
    ): JwtRepository {
        return JwtRepository(appSharePreference)
    }
}