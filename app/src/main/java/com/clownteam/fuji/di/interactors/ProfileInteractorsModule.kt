package com.clownteam.fuji.di.interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.fuji.api.FujiApi
import com.clownteam.profile_datasource.network.ProfileApi
import com.clownteam.profile_interactors.ProfileInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileInteractorsModule {

    @Singleton
    @Provides
    fun provideProfileApi(): ProfileApi {
        return FujiApi.createService(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileInteractors(
        profileApi: ProfileApi,
        tokenManager: TokenManager,
        userDataManager: UserDataManager
    ): ProfileInteractors {
        return ProfileInteractors.build(profileApi, tokenManager, userDataManager)
    }
}