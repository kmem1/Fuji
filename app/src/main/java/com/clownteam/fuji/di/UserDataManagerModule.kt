package com.clownteam.fuji.di

import com.clownteam.core.user_data.UserDataManager
import com.clownteam.fuji.auth.UserDataManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDataManagerModule {

    @Provides
    @Singleton
    fun provideUserManager(): UserDataManager {
        return UserDataManagerImpl()
    }
}