package com.clownteam.course_interactors

import com.clownteam.core.domain.DataState
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.core.domain.UIComponent
import com.clownteam.course_domain.Course
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMyCoursesUseCase : IGetMyCoursesUseCase {

    override fun invoke(): Flow<DataState<List<Course>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            delay(2000)

            emit(DataState.Data(data = testCourses))
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

    private val testCourses = listOf(
        Course(
            id = "583hgfhfgdsdfjtgj",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс"
        ),
        Course(
            id = "583hgfhfgdsdfjtgj",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            price = 0F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 396565,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс"
        ),
        Course(
            id = "583hgfhfgdsdfjtgj",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            price = 1000F,
            durationInMinutes = 36764,
            rating = 4.6F,
            marksCount = 6574,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс"
        )
    )
}

interface IGetMyCoursesUseCase : IUseCase.FlowOut<DataState<List<Course>>>