package com.clownteam.fuji.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable

@ExperimentalAnimationApi
@Composable
fun PresentModal(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}

@ExperimentalAnimationApi
@Composable
fun PresentNested(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutHorizontally() + fadeOut(),
        content = content,
        initiallyVisible = false
    )
}