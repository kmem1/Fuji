package com.clownteam.ui_courselist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.clownteam.components.utils.pluralResource
import com.clownteam.ui_courselist.R

@Composable
internal fun PriceText(price: Int) {
    val isFree = price == 0

    Text(
        modifier = Modifier.padding(top = 6.dp),
        text = if (isFree) {
            stringResource(R.string.free)
        } else {
            pluralResource(
                R.plurals.price_rubles,
                price,
                price
            )
        },
        style = if (isFree) MaterialTheme.typography.caption else MaterialTheme.typography.body1,
        fontWeight = FontWeight.W600,
        color = if (isFree) Color(0xFF2ED573) else Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}