package com.clownteam.ui_collectionaction.create_collection.di

import com.clownteam.collection_interactors.CollectionInteractors
import com.clownteam.collection_interactors.ICreateCollectionUseCase
import com.clownteam.collection_interactors.IUpdateCollectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CreateCollectionModule {

    @Provides
    @Singleton
    fun provideCreateCollectionUseCase(interactors: CollectionInteractors): ICreateCollectionUseCase {
        return interactors.createCollection
    }

    @Provides
    @Singleton
    fun provideUpdateCollectionUseCase(interactors: CollectionInteractors): IUpdateCollectionUseCase {
        return interactors.updateCollection
    }
}