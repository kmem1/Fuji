package com.clownteam.ui_courselist.di

import com.clownteam.course_interactors.CourseInteractors
import com.clownteam.course_interactors.IGetMyCoursesUseCase
import com.clownteam.course_interactors.IGetPopularCoursesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseListModule {

    @Provides
    @Singleton
    fun provideGetMyCoursesUseCase(interactors: CourseInteractors): IGetMyCoursesUseCase {
        return interactors.getMyCourses
    }

    @Provides
    @Singleton
    fun provideGetPopularCoursesUseCase(interactors: CourseInteractors): IGetPopularCoursesUseCase {
        return interactors.getPopularCourses
    }
}