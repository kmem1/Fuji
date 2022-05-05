package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.domain.SResult
import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_domain.Course

internal class GetMyCoursesUseCase(
    private val cache: CourseCache
) : IGetMyCoursesUseCase {

    override suspend fun invoke(): SResult<List<Course>> {
        return cache.getMyCourses()
    }
}

interface IGetMyCoursesUseCase : IUseCase.Out<SResult<List<Course>>>
