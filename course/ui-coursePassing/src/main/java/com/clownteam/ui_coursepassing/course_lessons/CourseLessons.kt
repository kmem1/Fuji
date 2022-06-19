package com.clownteam.ui_coursepassing.course_lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.course_domain.CourseLesson
import com.clownteam.ui_coursepassing.R

@Composable
fun CourseLessons(
    state: CourseLessonsState,
    eventHandler: EventHandler<CourseLessonsEvent>,
    onBack: () -> Unit = {},
    onLessonClick: (courseId: String, moduleId: String, lessonId: String, lessonName: String, stepId: String) -> Unit = { _, _, _, _, _ -> }
) {
    when (state) {
        is CourseLessonsState.Data -> {
            Column(modifier = Modifier.fillMaxSize()) {
                DefaultHeader(
                    titleText = stringResource(R.string.course_lessons_title_text),
                    onArrowClick = { onBack() })

                Spacer(modifier = Modifier.size(20.dp))

                if (state.moduleName.isNotEmpty()) {
                    Row(modifier = Modifier.height(IntrinsicSize.Min).padding(horizontal = 24.dp)) {
                        Box(
                            modifier = Modifier.width(1.dp).fillMaxHeight()
                                .background(Color(0xFF245ED1))
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Text(
                            state.moduleName,
                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.size(36.dp))
                }

                LessonList(
                    lessons = state.lessons
                ) { lessonId, lessonName, stepId ->
                    onLessonClick(state.courseId, state.moduleId, lessonId, lessonName, stepId)
                }
            }
        }

        CourseLessonsState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Retry",
                    onClick = { eventHandler.obtainEvent(CourseLessonsEvent.GetLessons) }
                )
            }
        }

        CourseLessonsState.Loading -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        CourseLessonsState.NetworkError -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Ошибка сети",
                    onClick = { eventHandler.obtainEvent(CourseLessonsEvent.GetLessons) }
                )
            }
        }

        CourseLessonsState.Unauthorized -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Необходимо авторизоваться",
                    onClick = { eventHandler.obtainEvent(CourseLessonsEvent.GetLessons) }
                )
            }
        }
    }
}

@Composable
private fun LessonList(
    lessons: List<CourseLesson>,
    onLessonClick: (lessonId: String, lessonName: String, stepId: String) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(lessons) { lesson ->
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = 65.dp).padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.primary)
                    .border(1.dp, Color.Yellow, RoundedCornerShape(16.dp))
                    .clickable { onLessonClick(lesson.id, lesson.title, lesson.currentStepId) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp).padding(end = 8.dp).weight(1F)
                ) {
                    Text(
                        lesson.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (lesson.currentProgress != 0) {
                        Text(
                            "${lesson.currentProgress}/${lesson.maxProgress}",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF3CFF2C)
                        )
                    }
                }

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).padding(start = 12.dp)
                )
            }
        }
    }
}