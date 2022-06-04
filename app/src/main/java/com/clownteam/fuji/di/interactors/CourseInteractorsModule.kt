package com.clownteam.fuji.di.interactors

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
    fun provideCourseInteractors(): CourseInteractors {
        return CourseInteractors.build(
            FujiApi.createService(CourseApi::class.java),
            FujiApi.BASE_URL
        )
    }
}