package com.clownteam.ui_profile.di

import com.clownteam.profile_interactors.IGetProfileUseCase
import com.clownteam.profile_interactors.ProfileInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideGetProfileUseCase(interactors: ProfileInteractors): IGetProfileUseCase {
        return interactors.getProfile
    }
}