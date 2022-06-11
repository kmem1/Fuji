package com.clownteam.ui_collectiondetailed.di

import com.clownteam.collection_interactors.CollectionInteractors
import com.clownteam.collection_interactors.IGetCollectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseDetailedModule {

    @Provides
    @Singleton
    fun provideGetCollectionUseCase(interactors: CollectionInteractors): IGetCollectionUseCase {
        return interactors.getCollection
    }
}