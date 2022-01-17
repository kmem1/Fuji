package com.example.ui_coursedetailed.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui_coursedetailed.R
import com.example.ui_coursedetailed.domain.ModuleItemUI

@Composable
internal fun ModulesColumn(moduleItems: List<ModuleItemUI>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // first moduleItem shouldn't have top padding
        var isFirst = true
        for (item in moduleItems) {
            var isExpanded by remember { mutableStateOf(false) }

            val topPadding = if (isFirst) {
                isFirst = false
                0.dp
            } else {
                6.dp
            }

            Column(modifier = Modifier.animateContentSize().padding(top = topPadding)) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(Color(0xFF424242))
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1F),
                        text = item.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W600
                    )

                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            painter = if (isExpanded) {
                                painterResource(R.drawable.ic_minus)
                            } else {
                                painterResource(R.drawable.ic_baseline_plus_24)
                            },
                            contentDescription = if (isExpanded) {
                                stringResource(R.string.hide_steps_button_content_description)
                            } else {
                                stringResource(R.string.show_steps_button_content_description)
                            }
                        )
                    }
                }

                if (isExpanded) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(Color(0xFF424242))
                            .padding(horizontal = 18.dp).padding(bottom = 18.dp)
                            .height(intrinsicSize = IntrinsicSize.Max)
                    ) {
                        Column(modifier = Modifier.fillMaxHeight()) {
                            Box(
                                modifier = Modifier.width(1.dp)
                                    .fillMaxHeight()
                                    .background(MaterialTheme.colors.background)
                            )
                        }

                        Column(modifier = Modifier.padding(start = 10.dp)) {
                            for (i in 0..item.steps.lastIndex) {
                                // last step shouldn't have bottom padding
                                val isLast = i == item.steps.lastIndex
                                Text(
                                    modifier = Modifier.padding(bottom = if (!isLast) 12.dp else 0.dp),
                                    text = "${i + 1}. ${item.steps[i]}",
                                    style = MaterialTheme.typography.subtitle2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}