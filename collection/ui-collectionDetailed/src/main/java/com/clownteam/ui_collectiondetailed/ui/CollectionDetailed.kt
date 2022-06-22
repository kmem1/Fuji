package com.clownteam.ui_collectiondetailed.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.components.AutoResizeText
import com.clownteam.components.DefaultButton
import com.clownteam.components.FontSizeRange
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.components.ColumnCourseListItem
import com.example.ui_collectiondetailed.R

@Composable
fun CollectionDetailed(
    state: CollectionDetailedState,
    eventHandler: EventHandler<CollectionDetailedEvent>,
    imageLoader: ImageLoader,
    onBackPressed: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    openCourse: (String) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is CollectionDetailedState.Data -> {
                CollectionDetailedContent(
                    state.collection,
                    imageLoader,
                    onBackPressed,
                    openCourse
                )
            }

            CollectionDetailedState.Error -> {
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Ошибка при получении данных")

                    Spacer(modifier = Modifier.size(24.dp))

                    DefaultButton(
                        text = "Повторить",
                        onClick = { eventHandler.obtainEvent(CollectionDetailedEvent.GetCollection) })
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
                        onClick = { navigateToLogin() })
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
    openCourse: (String) -> Unit
) {
    DefaultHeader(
        titleText = stringResource(R.string.collection_detailed_header_text),
        onArrowClick = { onBackPressed() },
        bgColor = MaterialTheme.colors.primary
    )

    CollectionDetailedHeader(collection, imageLoader)

    CollectionCoursesList(collection.courses, imageLoader, openCourse)
}

@Composable
fun CollectionDetailedHeader(collection: CourseCollection, imageLoader: ImageLoader) {
    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary)
            .padding(vertical = 14.dp)
    ) {
        Log.d("Kmem", collection.imageUrl)
        Image(
            modifier = Modifier.padding(start = 16.dp).size(80.dp).clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.primary),
            painter = rememberImagePainter(
                collection.imageUrl,
                imageLoader = imageLoader
            ),
            contentDescription = stringResource(R.string.course_collection_item_img_content_description),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1F).padding(start = 26.dp)) {
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
                    modifier = Modifier.size(22.dp).clip(CircleShape)
                        .background(MaterialTheme.colors.primary).align(Alignment.CenterVertically),
                    painter = rememberImagePainter(
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

        var ratingTextColor = Color.White
        var ratingBackgroundColor = Color.Gray

        when (collection.rating) {
            // Green
            in 4.0.rangeTo(5.0) -> {
                ratingTextColor = Color(0xFF2ED573)
                ratingBackgroundColor = Color(0xFF224430)
            }

            // Yellow
            in 3.0.rangeTo(3.99) -> {
                ratingTextColor = Color(0xFFFFC312)
                ratingBackgroundColor = Color(0xFF4C401D)
            }

            // Red
            in 1.0.rangeTo(2.99) -> {
                ratingTextColor = Color(0xFFFF5252)
                ratingBackgroundColor = Color(0xFF4C2929)
            }
        }

        Box(
            modifier = Modifier.padding(horizontal = 16.dp).height(32.dp).width(46.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ratingBackgroundColor)
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
fun CollectionCoursesList(
    courses: List<Course>,
    imageLoader: ImageLoader,
    navigateToDetailed: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(horizontal = 10.dp)) {
        itemsIndexed(courses) { index, course ->
            Spacer(modifier = Modifier.size(12.dp))

            ColumnCourseListItem(course, imageLoader, navigateToDetailed)

            if (index == courses.lastIndex) {
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}
