package com.clownteam.ui_authorization.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clownteam.components.utils.animateBorderColorAsState

@Composable
internal fun AuthorizationTextField(
    modifier: Modifier = Modifier,
    value: () -> String,
    onValueChange: (String) -> Unit,
    hint: String,
    isError: Boolean,
    errorText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        var isTextFieldFocused by remember { mutableStateOf(false) }

        val borderColor by animateBorderColorAsState(error = isError)

        BasicTextField(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                .background(MaterialTheme.colors.primary)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .onFocusChanged { isTextFieldFocused = it.isFocused },
            value = value(),
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
            cursorBrush = SolidColor(Color.White),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            maxLines = 1,
            singleLine = true,
            decorationBox = { innerTextView ->
                val animatedColor =
                    animateColorAsState(if (isTextFieldFocused) Color.Gray else Color.White)

                if (value().isEmpty()) {
                    Text(hint, color = animatedColor.value)
                }

                innerTextView()
            }
        )

        val shouldShowErrorText = isError && errorText.isNotBlank()
        AnimatedVisibility(
            shouldShowErrorText,
            modifier = Modifier.padding(top = 4.dp).align(Alignment.End),
        ) {
            Text(
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                text = errorText,
                textAlign = TextAlign.End
            )
        }
    }
}