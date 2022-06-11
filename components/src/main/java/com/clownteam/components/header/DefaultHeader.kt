package com.clownteam.components.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.R

@Composable
fun DefaultHeader(titleText: String, onArrowClick: () -> Unit, bgColor: Color = Color.Transparent) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth().background(bgColor)) {
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
                contentDescription = stringResource(R.string.back_button_content_description)
            )
        }

        Text(
            modifier = Modifier.padding(24.dp).constrainAs(title) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = titleText,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
    }
}