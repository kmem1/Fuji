package com.clownteam.fuji.di.interactors

import com.clownteam.collection_datasource.CollectionApi
import com.clownteam.collection_interactors.CollectionInteractors
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.fuji.api.FujiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CollectionInteractorsModule {

    @Provides
    @Singleton
    fun provideCollectionApi(): CollectionApi {
        return FujiApi.createService(CollectionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCollectionInteractors(
        collectionApi: CollectionApi,
        tokenManager: TokenManager,
        userDataManager: UserDataManager
    ): CollectionInteractors {
        return CollectionInteractors.build(collectionApi, FujiApi.BASE_URL, tokenManager, userDataManager)
    }
}