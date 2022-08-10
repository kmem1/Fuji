package com.clownteam.ui_courselist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.DefaultScreenUI
import com.clownteam.components.FontSizeRange
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.R
import com.clownteam.ui_courselist.components.ColumnCourseListItem
import com.clownteam.ui_courselist.components.CourseListLazyRow
import com.clownteam.ui_courselist.components.SimpleCourseListItem
import com.clownteam.ui_courselist.components.TitleText
import kotlinx.coroutines.launch

private sealed class NavigationRoute {
    class CourseDetail(val courseId: String) : NavigationRoute()
    class AddToCollection(val courseId: String) : NavigationRoute()
    object Login : NavigationRoute()
}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun CourseList(
    state: CourseListState,
    eventHandler: EventHandler<CourseListEvent>,
    navigateToDetailScreen: (String) -> Unit,
    navigateToAddToCollection: (String) -> Unit,
    navigateToLogin: () -> Unit,
    imageLoader: ImageLoader
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                is NavigationRoute.AddToCollection -> {
                    navigateToAddToCollection(it.courseId)
                }
                is NavigationRoute.CourseDetail -> {
                    navigateToDetailScreen(it.courseId)
                }
                NavigationRoute.Login -> {
                    navigateToLogin()
                }
            }
        }
    }

    DefaultScreenUI(
        progressBarState = if (state is CourseListState.Loading) ProgressBarState.Loading else ProgressBarState.Idle
    ) {
        val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val coroutineScope = rememberCoroutineScope()
        val selectedCourse = remember { mutableStateOf<Course?>(null) }
        ModalBottomSheetLayout(
            sheetState = bottomState,
            sheetContent = {
                BottomSheetContent(
                    selectedCourse.value,
                    imageLoader
                ) {
                    coroutineScope.launch {
                        bottomState.hide()
                        navigationRoute =
                            NavigationRoute.AddToCollection(selectedCourse.value?.id ?: "")

                        selectedCourse.value = null
                    }
                }
            },
            scrimColor = Color.Black.copy(alpha = 0.7F),
            sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            sheetBackgroundColor = MaterialTheme.colors.background
        ) {
            when (state) {
                is CourseListState.Data -> {
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
                                        course = item,
                                        imageLoader = imageLoader,
                                        onClick = { courseId ->
                                            navigationRoute = NavigationRoute.CourseDetail(courseId)
                                        }
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
                                    ColumnCourseListItem(
                                        course = course,
                                        imageLoader = imageLoader,
                                        onClick = {
                                            navigationRoute = NavigationRoute.CourseDetail(it)
                                        },
                                        onLongClick = { courseId ->
                                            coroutineScope.launch {
                                                selectedCourse.value =
                                                    state.popularCourses.find { it.id == courseId }

                                                bottomState.show()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }


                is CourseListState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = { eventHandler.obtainEvent(CourseListEvent.GetCourses) }) {
                            Text("Retry")
                        }
                    }
                }

                is CourseListState.Loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                CourseListState.Unauthorized -> {
                    navigationRoute = NavigationRoute.Login
                }
            }
        }
    }
}

@Composable
private fun BottomSheetContent(course: Course?, imageLoader: ImageLoader, onAddClick: () -> Unit) {
    if (course == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.bottom_sheet_error_text))
        }

        return
    }

    Column {
        Row(modifier = Modifier.padding(horizontal = 18.dp, vertical = 22.dp)) {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(CornerSize(12.dp)))
                    .background(MaterialTheme.colors.primary),
                painter = rememberAsyncImagePainter(
                    course.imgUrl,
                    imageLoader = imageLoader
                ),
                contentDescription = stringResource(R.string.course_image_content_description),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 22.dp)) {
                AutoResizeText(
                    text = course.title,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    fontSizeRange = FontSizeRange(min = 14.sp, max = 18.sp)
                )

                Spacer(modifier = Modifier.size(2.dp))

                Text(
                    text = course.authorName,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colors.secondary)
        )

        DefaultButton(
            stringResource(R.string.add_to_collection_btn_text),
            onClick = onAddClick,
            modifier = Modifier
                .padding(vertical = 34.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}



