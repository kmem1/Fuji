package com.clownteam.ui_profile.all_courses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.DefaultButton
import com.clownteam.components.PriceText
import com.clownteam.components.header.DefaultHeader
import com.clownteam.components.utils.getMembersCountString
import com.clownteam.core.domain.EventHandler
import com.clownteam.profile_domain.ProfileCourse
import com.clownteam.ui_profile.R

private sealed class NavigationRoute {
    class Course(val courseId: String) : NavigationRoute()
}

@Composable
fun AllProfileCoursesScreen(
    state: AllProfileCoursesScreenState,
    eventHandler: EventHandler<AllProfileCoursesScreenEvent>,
    imageLoader: ImageLoader,
    navigateBack: () -> Unit,
    navigateToCourse: (String) -> Unit = {},
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                is NavigationRoute.Course -> {
                    navigateToCourse(it.courseId)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DefaultHeader(
            titleText = stringResource(R.string.all_courses_title),
            onArrowClick = navigateBack
        )

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.errorMessage != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(text = state.errorMessage.asString(LocalContext.current))

                    Spacer(Modifier.size(8.dp))

                    DefaultButton(text = stringResource(R.string.retry)) {
                        eventHandler.obtainEvent(AllProfileCoursesScreenEvent.GetCourses)
                    }
                }
            }
        } else {
            if (state.courses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_courses_added),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                CoursesList(
                    state.courses,
                    imageLoader,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    onItemClick = { navigationRoute = NavigationRoute.Course(it.courseId) }
                )
            }
        }
    }
}

@Composable
fun CoursesList(
    courses: List<ProfileCourse>,
    imageLoader: ImageLoader,
    onItemClick: (ProfileCourse) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(courses) { course ->
            CourseListItem(
                item = course,
                imageLoader = imageLoader,
                modifier = Modifier.clickable { onItemClick(course) })
        }
    }
}

@Composable
fun CourseListItem(item: ProfileCourse, modifier: Modifier = Modifier, imageLoader: ImageLoader) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .background(MaterialTheme.colors.primary),
            painter = rememberAsyncImagePainter(
                item.imageUrl,
                imageLoader = imageLoader
            ),
            contentDescription = stringResource(id = R.string.course_image_content_description),
            contentScale = ContentScale.Crop
        )

        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Members count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Image(
                    modifier = Modifier.height(20.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_people_24),
                    contentDescription = stringResource(R.string.people_icon_content_description)
                )
                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = getMembersCountString(item.membersAmount),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium
                )
            }

            PriceText(item.price, modifier = Modifier.align(Alignment.TopCenter))

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = item.rating.toString(),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFF2FF5F)
                )

                Spacer(modifier = Modifier.size(2.dp))

                Image(
                    modifier = Modifier.height(20.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_star_rate_24),
                    contentDescription = stringResource(R.string.star_icon_content_description)
                )
            }
        }

        Text(
            text = item.authorName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}
