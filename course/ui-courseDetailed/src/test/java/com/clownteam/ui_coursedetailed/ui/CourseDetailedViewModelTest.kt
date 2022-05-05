package com.clownteam.ui_coursedetailed.ui

import androidx.lifecycle.SavedStateHandle
import com.clownteam.core.utils.extensions.toFailedResult
import com.clownteam.core.utils.extensions.toSuccessResult
import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfo
import com.clownteam.course_domain.toUIModel
import com.clownteam.course_interactors.IGetCourseByIdUseCase
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import com.clownteam.ui_coursedetailed.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CourseDetailedViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CourseDetailedViewModel
    private lateinit var getCourseUseCase: IGetCourseByIdUseCase
    private lateinit var getCourseInfoUseCase: IGetCourseInfoByIdUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val testCourseId = 1

    private val testCourse = Course(
        id = 1,
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        imgUrl = "https://i.ibb.co/LdhM7Bp/10.png",
        price = 1000F,
        durationInMinutes = 3676,
        rating = 4.6F,
        marksCount = 33456,
        membersAmount = 3965,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс",
        courseDurationInHours = 89
    )

    private val testCourseInfoUI = CourseInfo(
        courseId = 1,
        goalDescription = "qwe",
        forWhomCourseDescriptionItems = emptyList(),
        learningSkillsDescriptionItems = emptyList(),
        moduleItems = emptyList(),
        starsPercentage = emptyMap(),
        reviewItems = emptyList()
    ).toUIModel()

    private val testListener = object : CourseDetailedViewModel.Listener {
        val valuesTrace = ArrayList<CourseDetailedState>()

        override fun onStateChanged(newState: CourseDetailedState) {
            valuesTrace.add(newState)
        }
    }

    @Before
    fun setup() {
        getCourseUseCase = mock()
        getCourseInfoUseCase = mock()
        savedStateHandle = SavedStateHandle().apply {
            set(CourseDetailedViewModel.COURSE_ID_ARG_KEY, testCourseId)
        }

        testListener.valuesTrace.clear()
    }

    @Test
    fun should_haveDataState_onSuccessResult() = runBlocking {
        whenever(getCourseUseCase.invoke(testCourseId)).thenReturn(testCourse.toSuccessResult())
        whenever(getCourseInfoUseCase.invoke(testCourseId)).thenReturn(
            testCourseInfoUI.toSuccessResult()
        )

        viewModel = CourseDetailedViewModel(
            getCourseUseCase,
            getCourseInfoUseCase,
            savedStateHandle,
            testListener
        ).apply { obtainEvent(CourseDetailedEvent.GetCourseInfo) }

        val expected = arrayListOf(
            CourseDetailedState.Loading,
            CourseDetailedState.Data(testCourse, testCourseInfoUI)
        )

        Assert.assertEquals(expected, testListener.valuesTrace)
    }

    @Test
    fun should_haveErrorState_onFailedCourseResult() = runBlocking {
        whenever(getCourseUseCase.invoke(testCourseId)).thenReturn(testCourse.toFailedResult())
        whenever(getCourseInfoUseCase.invoke(testCourseId)).thenReturn(
            testCourseInfoUI.toSuccessResult()
        )

        viewModel = CourseDetailedViewModel(
            getCourseUseCase,
            getCourseInfoUseCase,
            savedStateHandle,
            testListener
        ).apply { obtainEvent(CourseDetailedEvent.GetCourseInfo) }

        val expected = arrayListOf(
            CourseDetailedState.Loading,
            CourseDetailedState.Error
        )

        Assert.assertEquals(expected, testListener.valuesTrace)
    }

    @Test
    fun should_haveErrorState_onFailedCourseInfoResult() = runBlocking {
        whenever(getCourseUseCase.invoke(testCourseId)).thenReturn(testCourse.toSuccessResult())
        whenever(getCourseInfoUseCase.invoke(testCourseId)).thenReturn(
            testCourseInfoUI.toFailedResult()
        )

        viewModel = CourseDetailedViewModel(
            getCourseUseCase,
            getCourseInfoUseCase,
            savedStateHandle,
            testListener
        ).apply { obtainEvent(CourseDetailedEvent.GetCourseInfo) }

        val expected = arrayListOf(
            CourseDetailedState.Loading,
            CourseDetailedState.Error
        )

        Assert.assertEquals(expected, testListener.valuesTrace)
    }

    @Test
    fun should_haveErrorState_onFailedResult() = runBlocking {
        whenever(getCourseUseCase.invoke(testCourseId)).thenReturn(testCourse.toFailedResult())
        whenever(getCourseInfoUseCase.invoke(testCourseId)).thenReturn(
            testCourseInfoUI.toFailedResult()
        )

        viewModel = CourseDetailedViewModel(
            getCourseUseCase,
            getCourseInfoUseCase,
            savedStateHandle,
            testListener
        ).apply { obtainEvent(CourseDetailedEvent.GetCourseInfo) }

        val expected = arrayListOf(
            CourseDetailedState.Loading,
            CourseDetailedState.Error
        )

        Assert.assertEquals(expected, testListener.valuesTrace)
    }

    @Test
    fun should_haveErrorState_onNullCourseId() = runBlocking {
        savedStateHandle = SavedStateHandle().apply {
            set(CourseDetailedViewModel.COURSE_ID_ARG_KEY, null)
        }

        viewModel = CourseDetailedViewModel(
            getCourseUseCase,
            getCourseInfoUseCase,
            savedStateHandle,
            testListener
        ).apply { obtainEvent(CourseDetailedEvent.GetCourseInfo) }

        val expected = CourseDetailedState.Error

        Assert.assertEquals(expected, testListener.valuesTrace[0])
    }
}