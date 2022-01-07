package com.clownteam.ui_courselist.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clownteam.components.utils.pluralResource
import com.clownteam.course_domain.Course
import com.clownteam.ui_courselist.R
import com.clownteam.ui_courselist.test_data.TestData

@Composable
internal fun CourseListItem(course: Course) {
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
            text = course.authorName,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Medium
        )
        RatingRow(rating = course.rating, marksCount = course.marksCount)
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = pluralResource(R.plurals.price_rubles, course.price.toInt(), course.price.toInt()),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.W600
        )
    }
}

@Composable
private fun RatingRow(
    rating: Float,
    marksCount: Int,
    modifier: Modifier = Modifier
) {
    Row {
        Text(
            text = rating.toString(),
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
            modifier = Modifier.padding(start = 3.dp),
            text = "($marksCount)",
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CourseListItem_Preview() {
    CourseListItem(TestData.testCourse)
}