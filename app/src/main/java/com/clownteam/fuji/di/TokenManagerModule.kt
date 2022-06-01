package com.clownteam.fuji.di

import com.clownteam.core.network.TokenManager
import com.clownteam.fuji.api.FujiApi
import com.clownteam.fuji.api.token.TokenService
import com.clownteam.fuji.auth.TokenManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TokenManagerModule {

    @Provides
    @Singleton
    fun provideTokenService(): TokenService {
        return FujiApi.createService(TokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenManager(tokenService: TokenService): TokenManager {
        return TokenManagerImpl(tokenService)
    }
}