package com.clownteam.ui_coursepassing.course_modules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.course_domain.CourseModule
import com.clownteam.ui_coursepassing.R

@Composable
fun CourseModules(
    state: CourseModulesState,
    eventHandler: EventHandler<CourseModulesEvent>,
    onBack: () -> Unit = {},
    onModuleClick: (courseId: String, moduleId: String, moduleName: String) -> Unit = { _, _, _ -> }
) {
    when (state) {
        is CourseModulesState.Data -> {
            if (state.course != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    DefaultHeader(
                        titleText = stringResource(R.string.course_modules_title_text),
                        onArrowClick = { onBack() }
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Row(modifier = Modifier.height(IntrinsicSize.Min).padding(horizontal = 24.dp)) {
                        Box(
                            modifier = Modifier.width(1.dp).fillMaxHeight()
                                .background(MaterialTheme.colors.secondary)
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Text(
                            state.course.title,
                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.size(36.dp))

                    ModulesList(
                        modules = state.modules,
                        onModuleClick = { moduleId, moduleName ->
                            onModuleClick(state.courseId ?: "", moduleId, moduleName)
                        }
                    )
                }
            }
        }

        CourseModulesState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Retry",
                    onClick = { eventHandler.obtainEvent(CourseModulesEvent.GetModules) }
                )
            }
        }

        CourseModulesState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        CourseModulesState.NetworkError -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DefaultButton(
                    text = "Ошибка сети",
                    onClick = { eventHandler.obtainEvent(CourseModulesEvent.GetModules) }
                )
            }
        }

        CourseModulesState.Unauthorized -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Необходимо авторизоваться",
                    onClick = { eventHandler.obtainEvent(CourseModulesEvent.GetModules) }
                )
            }
        }
    }
}

@Composable
private fun ModulesList(
    modules: List<CourseModule>,
    onModuleClick: (moduleId: String, moduleName: String) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(modules) { module ->
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = 65.dp).padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF245ED1))
                    .clickable { onModuleClick(module.id, module.title) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp).padding(end = 8.dp).weight(1F)
                ) {
                    Text(
                        module.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (module.currentProgress != 0) {
                        Text(
                            "${module.currentProgress}/${module.maxProgress}",
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFF3CFF2C)
                        )
                    }
                }

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).padding(start = 12.dp)
                )
            }
        }
    }
}