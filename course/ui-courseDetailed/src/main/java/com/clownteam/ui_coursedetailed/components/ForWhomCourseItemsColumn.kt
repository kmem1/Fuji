package com.clownteam.ui_coursedetailed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clownteam.course_domain.ForWhomCourseDescriptionItemUI

@Composable
internal fun ForWhomCourseItemsColumn(
    items: List<ForWhomCourseDescriptionItemUI>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        var isFirst = false
        for (item in items) {
            val topPadding = if (isFirst) {
                isFirst = false
                0.dp
            } else {
                10.dp
            }

            Column(modifier = Modifier.padding(top = topPadding)) {
                Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W600
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .background(MaterialTheme.colors.secondary)
                    )
                }

                Text(
                    text = item.description,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}