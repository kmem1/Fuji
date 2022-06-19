package com.clownteam.ui_coursepassing.course_steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.core.domain.EventHandler
import com.clownteam.course_domain.CourseStep

@Composable
fun CourseSteps(
    state: CourseStepsState,
    eventHandler: EventHandler<CourseStepsEvent>,
    onBack: () -> Unit = {}
) {
    when (state) {
        is CourseStepsState.Data -> {
            Column(modifier = Modifier.fillMaxSize()) {
                CourseLessonHeader(onBack, state.lessonTitle)

                Spacer(modifier = Modifier.size(24.dp))

                CourseStepsTopRow(
                    steps = state.steps,
                    currentStepIndex = state.currentStepIndex,
                    onStepClick = { step ->
                        eventHandler.obtainEvent(
                            CourseStepsEvent.UpdateCurrentStep(
                                step
                            )
                        )
                    },
                    modifier = Modifier.padding(start = 20.dp)
                )

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.currentStepContent)
                }
            }
        }

        is CourseStepsState.CurrentStepLoading -> {
            Column(modifier = Modifier.fillMaxSize()) {
                CourseLessonHeader(onBack, state.lessonTitle)

                Spacer(modifier = Modifier.size(24.dp))

                CourseStepsTopRow(
                    steps = state.steps,
                    currentStepIndex = state.currentStepIndex,
                    onStepClick = { step ->
                        eventHandler.obtainEvent(
                            CourseStepsEvent.UpdateCurrentStep(
                                step
                            )
                        )
                    },
                    modifier = Modifier.padding(start = 20.dp)
                )

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                }
            }
        }

        CourseStepsState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Retry",
                    onClick = { eventHandler.obtainEvent(CourseStepsEvent.GetSteps) }
                )
            }
        }

        CourseStepsState.Loading -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        CourseStepsState.NetworkError -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Ошибка сети",
                    onClick = { eventHandler.obtainEvent(CourseStepsEvent.GetSteps) }
                )
            }
        }

        CourseStepsState.Unauthorized -> {
            Box(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Необходимо авторизоваться",
                    onClick = { eventHandler.obtainEvent(CourseStepsEvent.GetSteps) }
                )
            }
        }
    }
}

@Composable
private fun CourseLessonHeader(onArrowClick: () -> Unit, lessonTitle: String) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (backIcon, title) = createRefs()

        IconButton(
            modifier = Modifier.padding(start = 24.dp).size(34.dp)
                .constrainAs(backIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            onClick = { onArrowClick() }
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(com.clownteam.components.R.string.back_button_content_description)
            )
        }

        Column(
            modifier = Modifier.width(IntrinsicSize.Max).padding(24.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                modifier = Modifier,
                text = lessonTitle,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.size(2.dp))

            Box(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Yellow))
        }
    }
}

@Composable
private fun CourseStepsTopRow(
    steps: List<CourseStep>,
    currentStepIndex: Int,
    onStepClick: (step: CourseStep) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        itemsIndexed(steps) { index, step ->
            val bgColor = if (step.isComplete) Color(0xFF2ED573) else Color(0xFF404040)
            val borderColor = if (index == currentStepIndex) Color.White else Color.Transparent
            Box(
                modifier = Modifier.size(28.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .border(3.dp, borderColor, RoundedCornerShape(10.dp))
                    .clickable { onStepClick(step) }
            )
        }
    }
}
