package com.example.ui_coursedetailed.di

import com.clownteam.course_interactors.CourseInteractors
import com.clownteam.course_interactors.IGetCourseByIdUseCase
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseDetailedModule {

    @Provides
    @Singleton
    fun provideGetCourseByIdUseCase(interactors: CourseInteractors): IGetCourseByIdUseCase {
        return interactors.getCourseById
    }

    @Provides
    @Singleton
    fun provideGetCourseInfoByIdUseCase(interactors: CourseInteractors): IGetCourseInfoByIdUseCase {
        return interactors.getCourseInfoById
    }
}