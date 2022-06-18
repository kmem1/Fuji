package com.clownteam.ui_coursepassing.course_modules.di

import com.clownteam.course_interactors.CourseInteractors
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import com.clownteam.course_interactors.IGetCourseModulesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseModulesModule {

    @Provides
    @Singleton
    fun provideGetCourseModulesUseCase(interactors: CourseInteractors): IGetCourseModulesUseCase {
        return interactors.getCourseModulesUseCase
    }

//    @Provides
//    @Singleton
//    fun provideGetCourseInfoByIdUseCase(interactors: CourseInteractors): IGetCourseInfoByIdUseCase {
//        return interactors.getCourseInfoById
//    }
}