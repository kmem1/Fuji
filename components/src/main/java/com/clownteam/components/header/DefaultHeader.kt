package com.clownteam.components.header

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.R

@Composable
fun DefaultHeader(
    titleText: String,
    showArrow: Boolean = true,
    onArrowClick: () -> Unit = {},
    bgColor: Color = Color.Transparent,
    @DrawableRes
    additionalIconResId: Int? = null,
    onAdditionalIconClick: () -> Unit = {}
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(bgColor)) {
        val (backIcon, title, additionalIcon) = createRefs()

        if (showArrow) {
            IconButton(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .size(34.dp)
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
        }

        Text(
            modifier = Modifier
                .padding(24.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = titleText,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        if (additionalIconResId != null) {
            IconButton(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(28.dp)
                    .constrainAs(additionalIcon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = { onAdditionalIconClick() }
            ) {
                Image(
                    painter = painterResource(id = additionalIconResId),
                    modifier = Modifier.size(28.dp),
                    contentDescription = stringResource(R.string.back_button_content_description)
                )
            }
        }
    }
}