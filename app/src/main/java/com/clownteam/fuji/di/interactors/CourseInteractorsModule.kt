package com.clownteam.fuji.di.interactors

import com.clownteam.course_interactors.CourseInteractors
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
        return CourseInteractors.build()
    }
}