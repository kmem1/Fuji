package com.clownteam.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.clownteam.components.utils.pluralResource

@Composable
fun PriceText(price: Int, modifier: Modifier = Modifier) {
    val isFree = price == 0

    Text(
        modifier = modifier,
        text = if (isFree) {
            stringResource(R.string.free)
        } else {
            pluralResource(
                R.plurals.price_rubles,
                price,
                price
            )
        },
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Medium,
        color = if (isFree) Color(0xFF2ED573) else MaterialTheme.colors.secondary,
        fontSize = 16.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}