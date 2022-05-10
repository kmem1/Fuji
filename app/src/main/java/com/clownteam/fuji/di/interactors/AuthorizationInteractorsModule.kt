package com.clownteam.fuji.di.interactors

import com.clownteam.authorization_interactors.AuthorizationInteractors
import com.clownteam.course_interactors.CourseInteractors
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
    fun provideAuthorizationInteractors(): AuthorizationInteractors {
        return AuthorizationInteractors.build()
    }
}