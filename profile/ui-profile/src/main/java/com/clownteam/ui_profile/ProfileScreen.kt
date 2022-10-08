package com.clownteam.ui_profile

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.FontSizeRange
import com.clownteam.components.PriceText
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.SResult
import com.clownteam.profile_domain.*

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    class Collection(val collectionId: String) : NavigationRoute()
    class Course(val courseId: String) : NavigationRoute()
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    eventHandler: EventHandler<ProfileEvent>,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit = {},
    navigateToCourse: (String) -> Unit = {},
    navigateToCollection: (String) -> Unit = {}
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Login -> {
                    navigateToLogin()
                }

                is NavigationRoute.Collection -> {
                    navigateToCollection(it.collectionId)
                }

                is NavigationRoute.Course -> {
                    navigateToCourse(it.courseId)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isNetworkError) {
            Column(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Retry",
                    onClick = { eventHandler.obtainEvent(ProfileEvent.GetProfile) })
            }
        } else {
            if (state.isUnauthorized) {
                navigationRoute = NavigationRoute.Login
            } else {
//                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
//                    val (username, exitButton) = createRefs()
//
//                    Text(
//                        text = state.profileData.username,
//                        modifier = Modifier.constrainAs(username) {
//                            top.linkTo(parent.top)
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
//                            bottom.linkTo(parent.bottom)
//                        })
//
//                    DefaultButton(
//                        text = "Выйти",
//                        onClick = { eventHandler.obtainEvent(ProfileEvent.SignOut) },
//                        modifier = Modifier
//                            .constrainAs(exitButton) {
//                                bottom.linkTo(parent.bottom)
//                                start.linkTo(parent.start)
//                                end.linkTo(parent.end)
//                            }
//                            .padding(bottom = 24.dp)
//                    )
//                }
                ProfileScreenContent(
                    state = state,
                    eventHandler = eventHandler,
                    imageLoader = imageLoader,
                    navigateToCourse = navigateToCourse,
                    navigateToCollection = navigateToCollection
                )
            }
        }
    }
}

@Composable
private fun ProfileScreenContent(
    state: ProfileState,
    eventHandler: EventHandler<ProfileEvent>,
    imageLoader: ImageLoader,
    navigateToCourse: (String) -> Unit,
    navigateToCollection: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (state.profileData) {
                is SResult.Success -> {
                    ProfileHeader(state.profileData.data, imageLoader)

                    Spacer(Modifier.size(24.dp))

                    ProfileStats(
                        state.profileData.data,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )

                    Spacer(Modifier.size(30.dp))

                    ProfileActivity(
                        state = state,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onActivityDayClick = { day, coords ->
                            Log.d("Kmem", "d: $day c: $coords")

                        }
                    )

                    Spacer(Modifier.size(30.dp))

                    ProfileCoursesColumn(
                        state = state,
                        imageLoader = imageLoader,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        onOpenAllClick = {},
                        onCourseClick = { navigateToCourse(it.courseId) }
                    )

                    Spacer(Modifier.size(30.dp))

                    ProfileCollectionsColumn(
                        state = state,
                        imageLoader = imageLoader,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        onOpenAllClick = {},
                        onCollectionClick = { navigateToCollection(it.collectionId) }
                    )

                    Spacer(Modifier.size(30.dp))
                }

                is SResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    profileData: ProfileData,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (bgImg, avatarColumn) = createRefs()

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .constrainAs(bgImg) { top.linkTo(parent.top) }
                .background(MaterialTheme.colors.primary),
            painter = rememberAsyncImagePainter(
                profileData.backgroundImageUrl,
                imageLoader = imageLoader
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // Column with avatar and nickname
        Column(
            modifier = Modifier.constrainAs(avatarColumn) {
                top.linkTo(parent.top, 90.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary),
                painter = rememberAsyncImagePainter(
                    profileData.avatarUrl,
                    imageLoader = imageLoader,
                    placeholder = null
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.size(16.dp))

            Text(
                text = profileData.username,
                style = MaterialTheme.typography.h1,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProfileStats(profileData: ProfileData, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 10.dp)
            .height(IntrinsicSize.Min)
    ) {
        val stats = listOf(
            StatsItem(id = 1, count = profileData.coursesCount, "Курсов"),
            StatsItem(id = 2, count = profileData.subscribersCount, "Подписчиков"),
            StatsItem(id = 3, count = profileData.subscriptionsCount, "Подписок")
        )

        for ((index, stat) in stats.withIndex()) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stat.count.toString(),
                    style = MaterialTheme.typography.h3,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.size(6.dp))

                Text(
                    stat.name,
                    style = MaterialTheme.typography.overline,
                    fontWeight = FontWeight.Medium
                )
            }

            // Divider
            if (index != stats.lastIndex) {
                Box(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colors.secondary)
                )
            }
        }
    }
}

private data class StatsItem(
    val id: Int,
    val count: Int,
    val name: String
)

@Composable
private fun ProfileActivity(
    state: ProfileState,
    modifier: Modifier = Modifier,
    onActivityDayClick: (ProfileActivityDay, ActivityDayCoordinates?) -> Unit
) {
    val activityList = state.profileData.data?.lastActivityList
    val activityListCoordinates = MutableList<ActivityDayCoordinates?>(60) { null }
    val interactionSource = remember { MutableInteractionSource() }

    if (!activityList.isNullOrEmpty()) {
        Column(modifier = modifier) {
            // Title row
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "Активность",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    " за последние 60 дней",
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }

            var currentSelectedDayIndex by remember { mutableStateOf(-1) }

            // Days column
            val rowsCount = 5
            val columnsCount = 12
            Column(
                modifier = Modifier
                    .padding(top = 22.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 0 until rowsCount) {
                    Row {
                        for (j in 0 until columnsCount) {
                            Log.d("Kmem", "composed ${i * columnsCount + j}")
                            val index = i * columnsCount + j
                            val day = activityList[index]
                            val bgColor = getColorByActivityDayType(type = day.type)

                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(bgColor)
//                                .onGloballyPositioned {
//                                    activityListCoordinates[index] = ActivityDayCoordinates(
//                                        top = it.boundsInRoot().top,
//                                        left = it.boundsInRoot().left,
//                                        right = it.boundsInRoot().right,
//                                        bottom = it.boundsInRoot().bottom
//                                    )
//                                }
//                                .clickable(
//                                    interactionSource = interactionSource,
//                                    indication = null
//                                ) {
//                                    currentSelectedDayIndex = index
//                                }
                            )
//                            {
//                                DropdownMenu(
//                                    expanded = index == currentSelectedDayIndex,
//                                    onDismissRequest = {
//                                        currentSelectedDayIndex = -1
//                                    },
//                                    properties = PopupProperties(focusable = false),
//                                    offset = DpOffset((-10).dp, (-50).dp)
//                                ) {
//                                    Box {
//                                        Text("Hello")
//                                    }
//                                }
//                            }

                            if (j != columnsCount - 1) Spacer(Modifier.size(7.dp))
                        }
                    }

                    if (i != rowsCount - 1) Spacer(Modifier.size(7.dp))
                }
            }

            Spacer(Modifier.size(10.dp))

            val totalCompletedTasksCount = activityList.sumOf { it.completedTasksCount }
            Text(
                text = "$totalCompletedTasksCount задач решено",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

private data class ActivityDayCoordinates(
    val top: Float,
    val left: Float,
    val right: Float,
    val bottom: Float
)

@Composable
private fun getColorByActivityDayType(type: ActivityDayType): Color {
    return when (type) {
        ActivityDayType.Level0 -> {
            Color(0xFF2A2A2A)
        }
        ActivityDayType.Level1 -> {
            Color(0xFF114828)
        }
        ActivityDayType.Level2 -> {
            Color(0xFF19693B)
        }
        ActivityDayType.Level3 -> {
            Color(0xFF2BBD69)
        }
        ActivityDayType.Level4 -> {
            Color(0xFF3AFF8C)
        }
    }
}

@Composable
private fun <T : Any> ProfileLazyRow(
    title: String,
    onOpenAllClick: () -> Unit,
    itemsResult: SResult<List<T>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable (item: T) -> Unit
) {
    Column(modifier = modifier.animateContentSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(R.string.open_all_text),
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.clickable { onOpenAllClick() }
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = contentPadding,
            modifier = Modifier.padding(top = 22.dp)
        ) {
            when (itemsResult) {
                is SResult.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is SResult.Success -> {
                    items(itemsResult.data) { item ->
                        itemContent(item)
                    }
                }

                is SResult.Failed -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = itemsResult.message.toString())
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun ProfileCoursesColumn(
    state: ProfileState,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onOpenAllClick: () -> Unit,
    onCourseClick: (item: ProfileCourse) -> Unit
) {
    ProfileLazyRow(
        title = stringResource(id = R.string.profile_courses_subtitle),
        onOpenAllClick = onOpenAllClick,
        modifier = modifier,
        contentPadding = contentPadding,
        itemsResult = state.profileCourses,
    ) { item ->
        ProfileCourseItem(course = item, imageLoader = imageLoader, onClick = onCourseClick)
    }
}

@Composable
private fun ProfileCourseItem(
    course: ProfileCourse,
    imageLoader: ImageLoader,
    onClick: (ProfileCourse) -> Unit
) {
    Column(
        modifier = Modifier
            .width(250.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.primary)
            .clickable { onClick(course) }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(122.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.primary),
            painter = rememberAsyncImagePainter(
                course.imageUrl,
                imageLoader = imageLoader
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            AutoResizeText(
                course.title,
                fontSizeRange = FontSizeRange(min = 13.sp, max = 16.sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(4.dp))

            Text(
                course.authorName,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )

            // Rating row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = course.rating.toString(),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFFF2FF5F)
                )

                Image(
                    modifier = Modifier.height(20.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_star_rate_24),
                    contentDescription = stringResource(R.string.star_icon_content_description)
                )

                Text(
                    text = "(${course.marksAmount})",
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W400
                )
            }

            PriceText(price = course.price)
        }
    }
}

@Composable
private fun ProfileCollectionsColumn(
    state: ProfileState,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onOpenAllClick: () -> Unit,
    onCollectionClick: (item: ProfileCollection) -> Unit
) {
    ProfileLazyRow(
        title = stringResource(id = R.string.profile_collections_subtitle),
        onOpenAllClick = onOpenAllClick,
        modifier = modifier,
        contentPadding = contentPadding,
        itemsResult = state.profileCollections,
    ) { item ->
        ProfileCollectionItem(
            collection = item,
            imageLoader = imageLoader,
            onClick = onCollectionClick
        )
    }
}

@Composable
fun ProfileCollectionItem(
    collection: ProfileCollection,
    imageLoader: ImageLoader,
    onClick: (ProfileCollection) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(250.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF282828))
            .clickable { onClick(collection) }
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .clip(RoundedCornerShape(12.dp)),
            painter = rememberAsyncImagePainter(collection.imageUrl, imageLoader = imageLoader),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 1.0F
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ) {
            Text(
                text = collection.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterVertically),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_collection_item_author_text),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1F),
                text = collection.authorName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
