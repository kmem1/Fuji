package com.clownteam.ui_courselist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.test_data.TestData

@Composable
internal fun SimpleCourseListItem(course: Course) {
    Column(modifier = Modifier.width(250.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(122.dp)
                .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                .background(Color.LightGray)
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

@Composable
@Preview(showBackground = true)
fun SimpleCourseListItem_Preview() {
    SimpleCourseListItem(TestData.testCourse)
}