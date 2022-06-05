package com.clownteam.ui_profile.di

import com.clownteam.profile_interactors.IGetProfileUseCase
import com.clownteam.profile_interactors.ISignOutUseCase
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

    @Provides
    @Singleton
    fun provideSignOutUseCase(interactors: ProfileInteractors): ISignOutUseCase {
        return interactors.signOut
    }
}