package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.domain.SResult
import com.clownteam.core.utils.extensions.emptyFailed
import com.clownteam.core.utils.extensions.toSuccessResult
import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_domain.CourseInfoUI
import com.clownteam.course_domain.toUIModel

internal class GetCourseInfoByIdUseCase(
    private val cache: CourseCache
) : IGetCourseInfoByIdUseCase {

    override suspend fun invoke(param: Int): SResult<CourseInfoUI> {
        val courseInfoResult = cache.getCourseInfo(param)

        return if (courseInfoResult is SResult.Success) {
            courseInfoResult.data.toUIModel().toSuccessResult()
        } else {
            emptyFailed()
        }
    }
}

interface IGetCourseInfoByIdUseCase : IUseCase.InOut<Int, SResult<CourseInfoUI>>