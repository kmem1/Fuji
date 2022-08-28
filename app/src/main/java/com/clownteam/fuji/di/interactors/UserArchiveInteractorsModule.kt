package com.clownteam.fuji.di.interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.fuji.api.FujiApi
import com.clownteam.user_archive_datasource.UserArchiveApi
import com.example.user_archive_interactors.UserArchiveInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserArchiveInteractorsModule {

    @Singleton
    @Provides
    fun provideUserArchiveApi(): UserArchiveApi {
        return FujiApi.createService(UserArchiveApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserArchiveInteractors(
        api: UserArchiveApi,
        tokenManager: TokenManager,
        userDataManager: UserDataManager
    ): UserArchiveInteractors {
        return UserArchiveInteractors.build(api, tokenManager, userDataManager, FujiApi.BASE_URL)
    }
}