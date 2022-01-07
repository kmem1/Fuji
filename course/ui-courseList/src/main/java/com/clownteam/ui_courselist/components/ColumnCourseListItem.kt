package com.clownteam.ui_courselist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.R
import com.clownteam.ui_courselist.test_data.TestData

@Composable
internal fun ColumnCourseListItem(course: Course) {
    // Main Row
    Row {
        // Avatar
        Image(
            modifier = Modifier
                .width(148.dp)
                .height(88.dp)
                .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                .background(Color.LightGray)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.cpp_course_image),
            contentDescription = stringResource(R.string.course_image_content_description),
            contentScale = ContentScale.Crop
        )

        // Content column
        Column(modifier = Modifier.padding(start = 9.dp)) {
            // Title
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                text = course.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            // Sub-Contents row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left sub-content
                Column {
                    // Rating
                    Row {
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
                    }

                    // Members count
                    Row {
                        Image(
                            modifier = Modifier.height(20.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_people_24),
                            contentDescription = stringResource(R.string.people_icon_content_description)
                        )
                        Text(
                            modifier = Modifier.padding(start = 3.dp),
                            text = getMembersCountString(course.membersAmount),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                // Right sub-content
                Column(
                    modifier = Modifier.padding(start = 24.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        Text(
                            text = course.authorName,
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row {
                        PriceText(course.price.toInt())
                    }
                }
            }
        }
    }
}

private fun getMembersCountString(count: Int) =
    if (count > 10000) {
        "${count / 1000}K"
    } else {
        "$count"
    }

@Composable
@Preview(showBackground = true)
private fun ColumnCourseListItem_Preview() {
    ColumnCourseListItem(TestData.testCourse)
}