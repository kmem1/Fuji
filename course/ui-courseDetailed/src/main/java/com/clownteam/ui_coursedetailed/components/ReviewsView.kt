package com.clownteam.ui_coursedetailed.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.clownteam.course_domain.ReviewItemUI
import com.clownteam.ui_coursedetailed.R

@Composable
internal fun ReviewsView(
    reviewItems: List<ReviewItemUI>,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader
) {
    var isFirst = true
    Column(modifier = modifier) {
        for (item in reviewItems) {
            val topPadding = if (isFirst) {
                isFirst = false
                0.dp
            } else {
                24.dp
            }

            Column(modifier = Modifier.padding(top = topPadding)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(41.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        painter = rememberImagePainter(
                            item.avatarUrl,
                            imageLoader = imageLoader
                        ),
                        contentDescription = stringResource(R.string.user_avatar_content_description),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Row {
                            Text(
                                text = item.userName,
                                style = MaterialTheme.typography.subtitle2,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.courseRating.toString(),
                                style = MaterialTheme.typography.subtitle2,
                                fontWeight = FontWeight.Medium
                            )

                            Icon(
                                Icons.Filled.Star,
                                modifier = Modifier.size(17.dp).padding(start = 2.dp),
                                contentDescription = stringResource(R.string.star_icon_content_description),
                                tint = Color(0xFFFFC312)
                            )

                            Text(
                                text = item.dateString,
                                modifier = Modifier.padding(start = 16.dp),
                                style = MaterialTheme.typography.overline,
                                color = Color(0xFFC6C6C6)
                            )
                        }
                    }
                }

                Row(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = item.content,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    }
}