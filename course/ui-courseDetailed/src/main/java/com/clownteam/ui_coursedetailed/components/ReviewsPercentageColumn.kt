package com.clownteam.ui_coursedetailed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun ReviewsPercentageView(percentageMap: Map<Int, Int>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        var isFirst = true
        for (pair in percentageMap) {
            val (mark, percentage) = pair
            val filledBoxWeight = percentage.toFloat()

            val topPadding = if (isFirst) {
                isFirst = false
                0.dp
            } else {
                2.dp
            }

            Row(
                modifier = Modifier.padding(top = topPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$mark",
                    modifier = Modifier.padding(end = 4.dp).width(12.dp),
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    textAlign = TextAlign.Start
                )

                if (filledBoxWeight > 0F) {
                    Box(
                        modifier = Modifier
                            .height(12.dp)
                            .weight(filledBoxWeight)
                            .background(MaterialTheme.colors.background)
                    )
                }

                if (filledBoxWeight != 100F) {
                    Box(
                        modifier = Modifier
                            .height(12.dp)
                            .weight(100F - filledBoxWeight)
                            .background(Color(0xFFC4C4C4))
                    )
                }

                Text(
                    text = "$percentage%",
                    modifier = Modifier.padding(start = 8.dp).width(40.dp),
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}