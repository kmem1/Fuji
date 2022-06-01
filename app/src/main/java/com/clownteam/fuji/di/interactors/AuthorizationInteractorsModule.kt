package com.clownteam.fuji.di.interactors

import com.clownteam.authorization_datasource.network.AuthorizationApi
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
    fun provideAuthorizationApi(): AuthorizationApi {
        return FujiApi.createService(AuthorizationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthorizationInteractors(authorizationApi: AuthorizationApi): AuthorizationInteractors {
        return AuthorizationInteractors.build(authorizationApi)
    }
}