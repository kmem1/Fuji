package com.clownteam.course_interactors

import com.clownteam.core.domain.DataState
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.core.domain.UIComponent
import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_domain.CourseInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GetCourseInfoByIdUseCase(
    private val cache: CourseCache
) : IGetCourseInfoByIdUseCase {

    override fun invoke(param: Int): Flow<DataState<CourseInfo>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val course = cache.getCourseInfo(param)

            emit(DataState.Data(data = course))
        } catch (e: Exception) {
            e.printStackTrace()

            emit(
                DataState.Response(
                    uiComponent = UIComponent.None(
                        message = e.message ?: "Unknown Error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}

interface IGetCourseInfoByIdUseCase : IUseCase.FlowInOut<Int, DataState<CourseInfo>>