package com.clownteam.ui_collectionlist.di

import com.clownteam.collection_interactors.CollectionInteractors
import com.clownteam.collection_interactors.IGetMyCollectionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CollectionListModule {

    @Provides
    @Singleton
    fun provideGetMyCollectionUseCase(interactors: CollectionInteractors): IGetMyCollectionsUseCase {
        return interactors.getMyCollections
    }
}