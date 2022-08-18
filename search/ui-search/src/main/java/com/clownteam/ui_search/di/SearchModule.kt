package com.clownteam.ui_search.di

import com.clownteam.search_interactors.IGetCollectionsByQueryUseCase
import com.clownteam.search_interactors.IGetCoursesByQueryUseCase
import com.clownteam.search_interactors.SearchInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideGetCoursesByQueryUseCase(interactors: SearchInteractors): IGetCoursesByQueryUseCase {
        return interactors.getCoursesByQuery
    }

    @Provides
    @Singleton
    fun provideGetCollectionsByQueryUseCase(interactors: SearchInteractors): IGetCollectionsByQueryUseCase {
        return interactors.getCollectionsByQuery
    }
}