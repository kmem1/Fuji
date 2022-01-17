package com.clownteam.ui_courselist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.clownteam.components.DefaultScreenUI
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.ui_courselist.R
import com.clownteam.ui_courselist.components.ColumnCourseListItem
import com.clownteam.ui_courselist.components.CourseListLazyRow
import com.clownteam.ui_courselist.components.SimpleCourseListItem
import com.clownteam.ui_courselist.components.TitleText

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun CourseList(
    state: CourseListState,
    eventHandler: EventHandler<CourseListEvent>,
    navigateToDetailScreen: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    DefaultScreenUI(
        progressBarState = if (state.isError) ProgressBarState.Idle else state.progressBarState
    ) {
        if (state.hasAllData && !state.isError) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (state.myCourses.isNotEmpty()) {
                    TitleText(stringResource(R.string.my_courses))
                    CourseListLazyRow(
                        modifier = Modifier.padding(top = 24.dp),
                        itemList = state.myCourses,
                        itemComposable = { item ->
                            SimpleCourseListItem(
                                item,
                                imageLoader,
                                navigateToDetailScreen
                            )
                        }
                    )
                }

                if (state.popularCourses.isNotEmpty()) {
                    TitleText(stringResource(R.string.popular))
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (course in state.popularCourses) {
                            ColumnCourseListItem(course, imageLoader)
                        }
                    }
                }
            }
        }

        if (state.isError) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {eventHandler.obtainEvent(CourseListEvent.GetCourses)}) {
                    Text("Retry")
                }
            }
        }
    }
}