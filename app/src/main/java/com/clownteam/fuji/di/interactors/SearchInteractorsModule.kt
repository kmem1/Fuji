package com.clownteam.fuji.di.interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.fuji.api.FujiApi
import com.clownteam.search_datasource.SearchApi
import com.clownteam.search_interactors.SearchInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchInteractorsModule {

    @Singleton
    @Provides
    fun provideSearchApi(): SearchApi {
        return FujiApi.createService(SearchApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSearchInteractors(
        searchApi: SearchApi,
        tokenManager: TokenManager
    ): SearchInteractors {
        return SearchInteractors.build(api = searchApi, baseUrl = FujiApi.BASE_URL, tokenManager)
    }
}