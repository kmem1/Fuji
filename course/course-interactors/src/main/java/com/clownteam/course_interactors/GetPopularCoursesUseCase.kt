package com.clownteam.course_interactors

import com.clownteam.core.domain.DataState
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.core.domain.UIComponent
import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_domain.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GetPopularCoursesUseCase(
    private val cache: CourseCache
) : IGetPopularCoursesUseCase {

    override fun invoke(): Flow<DataState<List<Course>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val courses = cache.getPopularCourses()

            emit(DataState.Data(data = courses))
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

interface IGetPopularCoursesUseCase : IUseCase.FlowOut<DataState<List<Course>>>