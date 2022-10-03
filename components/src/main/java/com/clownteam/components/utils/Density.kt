package com.clownteam.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Int.toDp(): Dp = LocalDensity.current.run { this@toDp.toDp() }

@Composable
fun Float.toDp(): Dp = LocalDensity.current.run { this@toDp.toDp() }