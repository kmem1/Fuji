package com.clownteam.ui_coursedetailed.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.clownteam.components.DefaultScreenUI
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.ui_coursedetailed.R
import com.clownteam.ui_coursedetailed.components.*

@Composable
fun CourseDetailed(
    state: CourseDetailedState,
    eventHandler: EventHandler<CourseDetailedEvent>,
    imageLoader: ImageLoader,
    onBack: () -> Unit
) {
    DefaultScreenUI(
        progressBarState = if (state is CourseDetailedState.Loading) ProgressBarState.Loading else ProgressBarState.Idle
    ) {
        if (state is CourseDetailedState.Data) {
            MainContent(state, imageLoader, onBack)
        }

        if (state is CourseDetailedState.Error) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { eventHandler.obtainEvent(CourseDetailedEvent.GetCourseInfo) }) {
                    Text("Retry")
                }
            }
        }

        if (state is CourseDetailedState.Unauthorized) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Необходима авторизация")
            }
        }
    }
}

@Composable
private fun MainContent(
    state: CourseDetailedState.Data,
    imageLoader: ImageLoader,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
            IconButton(
                modifier = Modifier.padding(vertical = 24.dp).size(34.dp),
                onClick = { onBack() }
            ) {
                Icon(
                    modifier = Modifier.size(34.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_content_description)
                )
            }

            Image(
                modifier = Modifier.fillMaxWidth()
                    .height(204.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                painter = rememberImagePainter(
                    state.course.imgUrl,
                    imageLoader = imageLoader
                ),
                contentDescription = stringResource(R.string.course_image_content_description),
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = state.course.title,
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )

            Text(
                text = state.course.authorName,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFAEAEAE)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.height(26.dp).width(28.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_star_rate_24),
                    contentDescription = stringResource(R.string.star_icon_content_description)
                )
                Text(
                    text = state.course.rating.toString(),
                    modifier = Modifier.padding(start = 2.dp),
                    style = MaterialTheme.typography.h3,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFFF2FF5F)
                )
                Text(
                    text = "(${state.course.marksCount} оценок)",
                    modifier = Modifier.padding(start = 10.dp),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W300
                )
            }

            Text(
                text = "${state.course.membersAmount} пользователей",
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W300
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${state.course.price}₽",
                    style = MaterialTheme.typography.h1,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.W600
                )

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF82C391),
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 56.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Купить",
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.W600,
                        fontSize = 24.sp
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .padding(horizontal = 14.dp, vertical = 30.dp)
        ) {
            if (state.courseInfo.goalDescription.isNotEmpty()) {
                TitleText("Цель курса")
                Text(
                    text = state.courseInfo.goalDescription,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(start = 14.dp, top = 6.dp)
                )
            }

            TitleText(
                stringResource(R.string.who_is_this_course_for),
                modifier = Modifier.padding(top = 12.dp)
            )

            ForWhomCourseItemsColumn(
                state.courseInfo.forWhomCourseDescriptionItems,
                modifier = Modifier.padding(start = 14.dp, top = 10.dp)
            )

            TitleText(
                stringResource(R.string.what_will_learn),
                modifier = Modifier.padding(top = 12.dp)
            )

            LearnignSkillsColumn(
                skillStrings = state.courseInfo.learningSkillsDescriptionItems,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (state.courseInfo.moduleItems.isNotEmpty()) {
                TitleText(
                    stringResource(R.string.course_program),
                    modifier = Modifier.padding(top = 12.dp)
                )

                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_schedule_24),
                        contentDescription = stringResource(id = R.string.clock_icon_content_description),
                        tint = Color(0xFF6CA7FF)
                    )
                    Text(
                        text = stringResource(
                            R.string.course_duration_hours,
                            state.course.courseDurationInHours
                        ),
                        modifier = Modifier.padding(start = 2.dp),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6CA7FF)
                    )
                }

                ModulesColumn(
                    state.courseInfo.moduleItems,
                    modifier = Modifier.padding(top = 24.dp)
                )

                ShowAllButton(
                    modifier = Modifier.padding(vertical = 32.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = {},
                    text = stringResource(R.string.all_program)
                )
            }

            TitleText(stringResource(R.string.reviews), modifier = Modifier.padding(top = 12.dp))

            Row(
                modifier = Modifier.height(IntrinsicSize.Min).padding(top = 16.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Star,
                    modifier = Modifier.size(40.dp),
                    contentDescription = stringResource(R.string.star_icon_content_description),
                    tint = Color(0xFFFFC312)
                )

                Text(
                    text = state.course.rating.toString(),
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.h1,
                    fontSize = 36.sp,
                    lineHeight = 56.sp
                )

                Text(
                    text = "(${state.course.marksCount})",
                    modifier = Modifier.padding(start = 14.dp, top = 6.dp),
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 18.sp,
                    color = Color(0xFFC6C6C6)
                )
            }

            ReviewsPercentageView(
                state.courseInfo.starsPercentage,
                modifier = Modifier.padding(top = 16.dp, start = 10.dp)
            )

            if (state.courseInfo.reviewItems.isNotEmpty()) {
                ReviewsView(
                    state.courseInfo.reviewItems,
                    modifier = Modifier.padding(top = 30.dp, start = 10.dp),
                    imageLoader = imageLoader
                )

                ShowAllButton(
                    modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally),
                    onClick = {},
                    text = stringResource(R.string.all_reviews)
                )
            }
        }
    }
}