package com.clownteam.ui_coursepassing.course_steps.di

import com.clownteam.course_interactors.CourseInteractors
import com.clownteam.course_interactors.IGetCourseStepUseCase
import com.clownteam.course_interactors.IGetCourseStepsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseStepsModule {

    @Provides
    @Singleton
    fun provideGetCourseStepsUseCase(interactors: CourseInteractors): IGetCourseStepsUseCase {
        return interactors.getCourseStepsUseCase
    }

    @Provides
    @Singleton
    fun provideGetCourseStepUseCase(interactors: CourseInteractors): IGetCourseStepUseCase {
        return interactors.getCourseStepUseCase
    }
}