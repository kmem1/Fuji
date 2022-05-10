package com.clownteam.ui_authorization.components

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun AuthorizationText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        modifier = modifier,
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.Medium
    )
}

@Composable
internal fun AuthorizationTextClickable(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        modifier = modifier.then(Modifier.clickable { onClick() }),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.Medium
    )
}