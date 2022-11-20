package com.clownteam.ui_profile.change_password

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.components.DefaultTextField
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_profile.R

@Composable
fun ChangePasswordScreen(
    state: ChangePasswordScreenState,
    eventHandler: EventHandler<ChangePasswordScreenEvent>,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.message) {
        if (state.message != null) {
            Toast.makeText(context, state.message.asString(context), Toast.LENGTH_SHORT).show()
            eventHandler.obtainEvent(ChangePasswordScreenEvent.MessageShown)
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (mainLayout, saveBtn) = createRefs()

        Column(modifier = Modifier.constrainAs(mainLayout) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            if (state.isLoading) {
                LinearProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            DefaultHeader(
                titleText = stringResource(R.string.change_profile_title_text),
                onArrowClick = { navigateBack() }
            )

            Spacer(Modifier.size(40.dp))

            TextFieldWithLabel(
                label = "Текущий пароль",
                value = state.currentPassword,
                onValueChange = {
                    eventHandler.obtainEvent(
                        ChangePasswordScreenEvent.SetCurrentPassword(it)
                    )
                },
                isError = state.currentPasswordError != null,
                errorText = state.currentPasswordError?.asString(context) ?: "",
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(Modifier.size(12.dp))

            TextFieldWithLabel(
                label = "Новый пароль",
                value = state.newPassword,
                onValueChange = {
                    eventHandler.obtainEvent(
                        ChangePasswordScreenEvent.SetNewPassword(it)
                    )
                },
                isError = state.newPasswordError != null,
                errorText = state.newPasswordError?.asString(context) ?: "",
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
        }

        DefaultButton(
            text = stringResource(R.string.save),
            modifier = Modifier.constrainAs(saveBtn) {
                bottom.linkTo(parent.bottom, 30.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = !state.isLoading
        ) {
            eventHandler.obtainEvent(ChangePasswordScreenEvent.ApplyChanges)
        }
    }
}

@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.size(8.dp))

        DefaultTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            hint = "",
            isError = isError,
            errorText = errorText,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation
        )
    }
}