package com.clownteam.fuji.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.clownteam.fuji.R

val Gotham = FontFamily(
    Font(R.font.gotham_light, FontWeight.W400),
    Font(R.font.gotham_thin, FontWeight.W300),
    Font(R.font.gotham_bold, FontWeight.W600),
    Font(R.font.gotham_medium, FontWeight.Medium),
    Font(R.font.gotham_bold, FontWeight.Bold)
)

val GothamTypography = Typography(
    h1 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
    ),
    h2 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Medium,
        fontSize = 26.sp,
    ),
    h3 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
    ),
    h4 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp,
    ),
    h5 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
    ),
    h6 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    body1 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Gotham,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        color = Color.White
    ),
    caption = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    overline = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.W400,
        fontSize = 13.sp
    )
)