package com.clownteam.ui_authorization.restore_password

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_authorization.R
import com.clownteam.ui_authorization.components.AuthorizationTextField

@Composable
fun RestorePasswordScreen(
    state: RestorePasswordState,
    eventHandler: EventHandler<RestorePasswordEvent>,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    DefaultHeader(titleText = "Восстановление пароля", onArrowClick = { navigateBack() })

    if (state.isSuccess) {
        RestorePasswordSuccessScreen(onButtonClick = navigateBack)
    } else {
        RestorePasswordEmailScreen(context, state, eventHandler)
    }
}

@Composable
private fun RestorePasswordEmailScreen(
    context: Context,
    state: RestorePasswordState,
    eventHandler: EventHandler<RestorePasswordEvent>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.restore_password_info_text),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(26.dp))

        AuthorizationTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            value = state.email,
            onValueChange = { eventHandler.obtainEvent(RestorePasswordEvent.EmailChanged(it)) },
            hint = stringResource(R.string.email_hint),
            isError = state.emailError != null,
            errorText = state.emailError?.asString(context) ?: "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.size(90.dp))

        DefaultButton(text = stringResource(R.string.restore_password_button_text)) {
            eventHandler.obtainEvent(RestorePasswordEvent.Submit)
        }
    }
}

@Composable
private fun RestorePasswordSuccessScreen(onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.restore_password_success_text),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(60.dp))

        DefaultButton(text = "Ок", onClick = { onButtonClick() })
    }
}