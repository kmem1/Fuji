package com.clownteam.fuji.di.interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.course_datasource.network.CourseApi
import com.clownteam.course_interactors.CourseInteractors
import com.clownteam.fuji.api.FujiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CourseInteractorsModule {

    @Provides
    @Singleton
    fun provideCourseInteractors(
        tokenManager: TokenManager,
        userDataManager: UserDataManager
    ): CourseInteractors {
        return CourseInteractors.build(
            api = FujiApi.createService(CourseApi::class.java),
            baseUrl = FujiApi.BASE_URL,
            tokenManager = tokenManager,
            userDataManager = userDataManager
        )
    }
}