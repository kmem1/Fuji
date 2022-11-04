package com.clownteam.ui_profile.di

import com.clownteam.core.interactors.IValidateLoginUseCase
import com.clownteam.profile_interactors.*
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
    fun provideGetProfileCoursesUseCase(interactors: ProfileInteractors): IGetProfileCoursesUseCase {
        return interactors.getProfileCourses
    }

    @Provides
    @Singleton
    fun provideGetProfileCollectionsUseCase(interactors: ProfileInteractors): IGetProfileCollectionsUseCase {
        return interactors.getProfileCollections
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(interactors: ProfileInteractors): ISignOutUseCase {
        return interactors.signOut
    }
}
