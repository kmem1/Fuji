package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.domain.SResult
import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_domain.Course

internal class GetCourseByIdUseCase(
    private val cache: CourseCache
) : IGetCourseByIdUseCase {

    override suspend fun invoke(param: Int): SResult<Course> {
        return cache.getCourse(param)
    }
}

interface IGetCourseByIdUseCase : IUseCase.InOut<Int, SResult<Course>>