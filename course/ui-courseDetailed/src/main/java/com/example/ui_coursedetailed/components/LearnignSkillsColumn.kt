package com.example.ui_coursedetailed.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui_coursedetailed.R

@Composable
fun LearnignSkillsColumn(skillStrings: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        var isFirst = false
        for (skillString in skillStrings) {
            val topPadding = if (isFirst) {
                isFirst = false
                0.dp
            } else {
                8.dp
            }

            Row(modifier = Modifier.padding(top = topPadding)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_triangle_24),
                    contentDescription = stringResource(R.string.triangle_icon_content_description),
                    tint = MaterialTheme.colors.background,
                    modifier = Modifier.size(36.dp).align(Alignment.CenterVertically)
                )

                Text(
                    text = skillString,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}