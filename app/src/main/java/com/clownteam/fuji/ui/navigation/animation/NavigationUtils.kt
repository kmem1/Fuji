package com.clownteam.fuji.ui.navigation.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun defaultEnterTransition(): EnterTransition = slideInHorizontally { it }

fun defaultExitTransition(): ExitTransition = slideOutHorizontally { -it }

fun defaultPopEnterTransition(): EnterTransition = slideInHorizontally { -it }

fun defaultPopExitTransition(): ExitTransition = slideOutHorizontally { it }