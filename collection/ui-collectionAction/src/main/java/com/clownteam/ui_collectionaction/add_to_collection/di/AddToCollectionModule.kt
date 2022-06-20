package com.clownteam.ui_collectionaction.add_to_collection.di

import com.clownteam.collection_interactors.CollectionInteractors
import com.clownteam.collection_interactors.IGetUserCollectionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AddToCollectionModule {

    @Singleton
    @Provides
    fun provideGetUserCollectionsUseCase(interactors: CollectionInteractors): IGetUserCollectionsUseCase {
        return interactors.getUserCollections
    }
}