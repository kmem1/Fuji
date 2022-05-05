package com.clownteam.ui_coursedetailed.ui

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import com.clownteam.core.domain.EventHandler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class CourseDetailedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var state = CourseDetailedState.Loading
    private lateinit var context: Context

    private val fakeEventHandler = object : EventHandler<CourseDetailedEvent> {
        override fun obtainEvent(event: CourseDetailedEvent) { }
    }

    private lateinit var fakeImageLoader: ImageLoader

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        fakeImageLoader = ImageLoader(context)
    }

    @Test
    fun should_showCircularProgressBar_onLoadingState() {
        state = CourseDetailedState.Loading

        composeTestRule.setContent {
            MaterialTheme {
                CourseDetailed(state, fakeEventHandler, fakeImageLoader) {}
            }
        }

        composeTestRule.onNodeWithTag("Loading ProgressBar").assertExists()
    }
}