package com.clownteam.components.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

@Composable
fun animateBorderColorAsState(
    enabled: Boolean = true,
    error: Boolean = false,
    active: Boolean = false,
    completed: Boolean = false,
    animationSpec: AnimationSpec<Color> = spring()
): State<Color> {
    val targetValue = getBorderColor(
        isEnabled = enabled,
        isError = error,
        isActive = active,
        isCompleted = completed
    )

    return animateColorAsState(targetValue = targetValue, animationSpec = animationSpec)
}

@Composable
fun getBorderColor(
    isEnabled: Boolean,
    isError: Boolean,
    isActive: Boolean,
    isCompleted: Boolean
): Color {
    return when {
        !isEnabled -> MaterialTheme.colors.primary
        isError -> MaterialTheme.colors.error
        isCompleted -> MaterialTheme.colors.primary
        isActive -> MaterialTheme.colors.primary
        else -> MaterialTheme.colors.primary
    }
}
