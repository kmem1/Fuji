package com.example.ui_user_archive.di

import com.example.user_archive_interactors.IGetArchiveCollectionsUseCase
import com.example.user_archive_interactors.IGetArchiveCoursesUseCase
import com.example.user_archive_interactors.UserArchiveInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserArchiveModule {

    @Provides
    @Singleton
    fun provideGetArchiveCoursesUseCase(interactors: UserArchiveInteractors): IGetArchiveCoursesUseCase {
        return interactors.getArchiveCourses
    }

    @Provides
    @Singleton
    fun provideGetArchiveCollectionsUseCase(interactors: UserArchiveInteractors): IGetArchiveCollectionsUseCase {
        return interactors.getArchiveCollections
    }
}