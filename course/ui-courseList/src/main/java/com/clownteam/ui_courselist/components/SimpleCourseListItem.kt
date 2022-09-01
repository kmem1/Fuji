package com.clownteam.ui_courselist.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleCourseListItem(
    course: Course,
    imageLoader: ImageLoader,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    val roundedTopCornersShape = RoundedCornerShape(
        topStart = CornerSize(16.dp),
        topEnd = CornerSize(16.dp),
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    Column(modifier = Modifier
        .width(240.dp)
        .clip(roundedTopCornersShape)
        .combinedClickable(
            onClick = { onClick(course.id) },
            onLongClick = { onLongClick(course.id) }
        )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(125.dp)
                .clip(RoundedCornerShape(CornerSize(16.dp)))
                .background(MaterialTheme.colors.primary),
            painter = rememberAsyncImagePainter(
                course.imgUrl,
                imageLoader = imageLoader
            ),
            contentDescription = stringResource(R.string.course_image_content_description),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth(),
            text = course.title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${course.currentPoints}/${course.maxProgressPoints}",
            color = Color(0xFF3CFF2C),
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Medium
        )
    }
}