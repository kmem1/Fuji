package com.clownteam.ui_courselist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clownteam.components.DefaultScreenUI
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_courselist.R
import com.clownteam.ui_courselist.components.ColumnCourseListItem
import com.clownteam.ui_courselist.components.CourseListLazyRow
import com.clownteam.ui_courselist.components.SimpleCourseListItem
import com.clownteam.ui_courselist.components.TitleText
import com.clownteam.ui_courselist.test_data.TestData

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun CourseList(
    state: CourseListState,
    eventHandler: EventHandler<CourseListEvent>,
    navigateToDetailScreen: (String) -> Unit
) {
    DefaultScreenUI(progressBarState = state.progressBarState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(visible = state.myCourses.isNotEmpty()) {
                Column {
                    TitleText(stringResource(R.string.my_courses))
                    CourseListLazyRow(
                        modifier = Modifier.padding(top = 24.dp),
                        itemList = state.myCourses,
                        itemComposable = { item -> SimpleCourseListItem(item) }
                    )
                }
            }

            AnimatedVisibility(visible = state.popularCourses.isNotEmpty()) {
                Column {
                    TitleText(stringResource(R.string.popular))
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (course in state.popularCourses) {
                            ColumnCourseListItem(course)
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
@Preview(showBackground = true)
fun CourseListPreview() {
    CourseList(
        state = CourseListState(myCourses = TestData.testCourses),
        eventHandler = object : EventHandler<CourseListEvent> {
            override fun obtainEvent(event: CourseListEvent) {}
        },
        navigateToDetailScreen = { _ -> }
    )
}