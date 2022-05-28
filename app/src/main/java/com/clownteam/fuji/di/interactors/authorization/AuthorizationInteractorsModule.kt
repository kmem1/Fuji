package com.clownteam.fuji.di.interactors.authorization

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.authorization_interactors.AuthorizationInteractors
import com.clownteam.fuji.api.FujiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthorizationInteractorsModule {

    @Provides
    @Singleton
    fun provideAuthorizationService(): AuthorizationService {
        return AuthorizationServiceImpl(FujiApi.service)
    }

    @Provides
    @Singleton
    fun provideAuthorizationInteractors(authorizationService: AuthorizationService): AuthorizationInteractors {
        return AuthorizationInteractors.build(authorizationService)
    }
}