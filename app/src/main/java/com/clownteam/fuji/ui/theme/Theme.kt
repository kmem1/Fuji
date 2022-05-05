package com.clownteam.fuji.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val DarkColorPalette = darkColors(
//    primary = DarkGray,
//    primaryVariant = Color.Black,
//    onPrimary = Color.White,
//    secondary = Color.Black,
//    secondaryVariant = Color.Black,
//    onSecondary = Color.White,
//    error = RedErrorLight,
//    onError = RedErrorDark,
//    background = Purple,
//    onBackground = Color.White,
//    surface = Color.White,
//    onSurface = Color.Black,
//)

private val LightColorPalette = lightColors(
    primary = BlackLight3,
    primaryVariant = Color.Black,
    onPrimary = Color.White,
    secondary = Color.Black,
    secondaryVariant = Color.Black,
    onSecondary = Color.White,
    error = RedErrorLight,
    onError = RedErrorDark,
    background = BlackLight1,
    onBackground = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
)

@Composable
fun FujiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        content = content,
        typography = MontserratTypography,
        colors = LightColorPalette
    )
}