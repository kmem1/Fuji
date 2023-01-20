package com.clownteam.ui_collectiondetailed.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.FontSizeRange
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.components.ColumnCourseListItem
import com.example.ui_collectiondetailed.R

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    class CourseDetailed(val courseId: String) : NavigationRoute()
    class EditCollection(val collectionId: String) : NavigationRoute()
}

@Composable
fun CollectionDetailed(
    state: CollectionDetailedState,
    eventHandler: EventHandler<CollectionDetailedEvent>,
    imageLoader: ImageLoader,
    onBackPressed: () -> Unit,
    navigateToLogin: () -> Unit,
    openCourse: (String) -> Unit,
    navigateToEdit: (String) -> Unit
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Login -> {
                    navigateToLogin()
                }
                is NavigationRoute.CourseDetailed -> {
                    openCourse(it.courseId)
                }
                is NavigationRoute.EditCollection -> {
                    navigateToEdit(it.collectionId)
                }
            }
        }
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                eventHandler.obtainEvent(CollectionDetailedEvent.GetCollection(showLoading = false))
            }
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is CollectionDetailedState.Data -> {
                CollectionDetailedContent(
                    collection = state.collection,
                    imageLoader = imageLoader,
                    onBackPressed = onBackPressed,
                    openCourse = { navigationRoute = NavigationRoute.CourseDetailed(it) },
                    navigateToEdit = {
                        navigationRoute = NavigationRoute.EditCollection(state.collection.id)
                    },
                    isRateCollectionLoading = state.isRateCollectionLoading,
                    rateCollection = {
                        eventHandler.obtainEvent(CollectionDetailedEvent.RateCollection(it))
                    }
                )
            }

            CollectionDetailedState.Error -> {
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Ошибка при получении данных")

                    Spacer(modifier = Modifier.size(24.dp))

                    DefaultButton(
                        text = "Повторить",
                        onClick = {
                            eventHandler.obtainEvent(CollectionDetailedEvent.GetCollection())
                        }
                    )
                }
            }

            CollectionDetailedState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            CollectionDetailedState.Unauthorized -> {
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Необходимо авторизоваться")

                    Spacer(modifier = Modifier.size(24.dp))

                    DefaultButton(
                        text = "Авторизоваться",
                        onClick = { navigationRoute = NavigationRoute.Login }
                    )
                }
            }
        }
    }
}

@Composable
fun CollectionDetailedContent(
    collection: CourseCollection,
    imageLoader: ImageLoader,
    onBackPressed: () -> Unit,
    openCourse: (String) -> Unit,
    navigateToEdit: () -> Unit,
    isRateCollectionLoading: Boolean,
    rateCollection: (Int) -> Unit
) {
    var showRateDialog by remember { mutableStateOf(false) }

    if (showRateDialog) {
        RateCollectionDialog(
            onDismiss = { showRateDialog = false },
            onRateClick = rateCollection,
            isLoading = isRateCollectionLoading
        )
    }

    DefaultHeader(
        titleText = stringResource(R.string.collection_detailed_header_text),
        onArrowClick = { onBackPressed() },
        bgColor = MaterialTheme.colors.primary,
        additionalIconResId = if (collection.isEditable) R.drawable.ic_edit else null,
        onAdditionalIconClick = { navigateToEdit() }
    )

    CollectionDetailedHeader(collection, imageLoader, onRatingClick = { showRateDialog = true })

    CollectionCoursesList(collection.courses, imageLoader, openCourse)
}

@Composable
fun RateCollectionDialog(
    onDismiss: () -> Unit,
    onRateClick: (Int) -> Unit,
    isLoading: Boolean
) {
    var currentSelectedRating by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                    .padding(horizontal = 34.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1F1F1F))
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вы ещё не оценили подборку",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.size(26.dp))

            SelectRatingRow(onMarkSelected = { currentSelectedRating = it })

            Spacer(Modifier.size(26.dp))

            DefaultButton(text = "Оценить", enabled = currentSelectedRating != 0 && !isLoading) {
                onRateClick(currentSelectedRating)
            }
        }
    }
}

@Composable
fun SelectRatingRow(onMarkSelected: (Int) -> Unit) {
    var currentSelectedRating by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
            .padding(6.dp)
    ) {
        repeat(5) { index ->
            if (index != 0) {
                Spacer(Modifier.size(6.dp))
            }

            val mark = index + 1

            Box(
                modifier = Modifier
                    .width(27.dp)
                    .height(31.dp)
                    .clip(CircleShape)
                    .background(
                        if (currentSelectedRating == mark)
                            getCollectionRatingColor(rating = mark.toDouble())
                        else
                            Color.Transparent
                    )
                    .clickable {
                        currentSelectedRating = mark
                        onMarkSelected(mark)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${index + 1}",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CollectionDetailedHeader(
    collection: CourseCollection,
    imageLoader: ImageLoader,
    onRatingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 14.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.primary),
            painter = rememberAsyncImagePainter(
                collection.imageUrl,
                imageLoader = imageLoader
            ),
            contentDescription = stringResource(R.string.course_collection_item_img_content_description),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1F)
                .padding(start = 26.dp)
        ) {
            AutoResizeText(
                text = collection.title,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                maxLines = if (collection.title.split(" ").size == 1) 1 else 2,
                overflow = TextOverflow.Ellipsis,
                fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
            )

            Row(modifier = Modifier.padding(top = 12.dp)) {
                Image(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .align(Alignment.CenterVertically),
                    painter = rememberAsyncImagePainter(
                        collection.author.avatar_url,
                        imageLoader = imageLoader
                    ),
                    contentDescription = stringResource(R.string.course_collection_item_img_content_description),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = collection.author.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        val ratingTextColor = getCollectionRatingColor(collection.rating)
        var ratingBackgroundColor = Color.Gray

        when (collection.rating) {
            // Green
            in 4.0.rangeTo(5.0) -> {
                ratingBackgroundColor = Color(0xFF224430)
            }

            // Yellow
            in 3.0.rangeTo(3.99) -> {
                ratingBackgroundColor = Color(0xFF4C401D)
            }

            // Red
            in 1.0.rangeTo(2.99) -> {
                ratingBackgroundColor = Color(0xFF4C2929)
            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(32.dp)
                .width(46.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ratingBackgroundColor)
                .clickable { onRatingClick() }
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = String.format("%.1f", collection.rating),
                color = ratingTextColor,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun getCollectionRatingColor(rating: Double): Color {
    when (rating) {
        // Green
        in 4.0.rangeTo(5.0) -> {
            return Color(0xFF2ED573)
        }

        // Yellow
        in 3.0.rangeTo(3.99) -> {
            return Color(0xFFFFC312)
        }

        // Red
        in 1.0.rangeTo(2.99) -> {
            return Color(0xFFFF5252)
        }
    }

    return Color.White
}

@Composable
fun CollectionCoursesList(
    courses: List<Course>,
    imageLoader: ImageLoader,
    navigateToDetailed: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 10.dp)
    ) {
        itemsIndexed(courses) { index, course ->
            Spacer(modifier = Modifier.size(12.dp))

            ColumnCourseListItem(course, imageLoader, navigateToDetailed)

            if (index == courses.lastIndex) {
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}