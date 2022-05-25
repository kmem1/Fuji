package com.clownteam.ui_authorization.registration

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_authorization.R
import com.clownteam.ui_authorization.components.AuthorizationText
import com.clownteam.ui_authorization.components.AuthorizationTextClickable
import com.clownteam.ui_authorization.components.AuthorizationTextField
import kotlinx.coroutines.flow.collect

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    eventHandler: EventHandler<RegistrationEvent>,
    viewModel: RegistrationViewModel,
    navigateToLogin: () -> Unit = {},
    onSuccessRegistration: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is RegistrationViewModel.ValidationEvent.Success -> onSuccessRegistration()
            }
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        val (title, centerLayout, bottomLayout) = createRefs()

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                bottom.linkTo(centerLayout.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(R.string.registration_title),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Column(modifier = Modifier.constrainAs(centerLayout) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            AuthorizationTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                value = state.login,
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.LoginChanged(it)) },
                hint = stringResource(R.string.login_hint),
                isError = state.loginError != null,
                errorText = state.loginError?.asString(context) ?: ""
            )

            Spacer(modifier = Modifier.size(18.dp))

            AuthorizationTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                value = state.email,
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.EmailChanged(it)) },
                hint = stringResource(R.string.email_hint),
                isError = state.emailError != null,
                errorText = state.emailError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.size(18.dp))

            AuthorizationTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                value = state.password,
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.PasswordChanged(it)) },
                hint = stringResource(R.string.password_hint),
                isError = state.passwordError != null,
                errorText = state.passwordError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.size(18.dp))

            AuthorizationTextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                value = state.repeatedPassword,
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.RepeatedPasswordChanged(it)) },
                hint = stringResource(R.string.repeated_password_hint),
                isError = state.repeatedPasswordError != null,
                errorText = state.repeatedPasswordError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Column(modifier = Modifier.constrainAs(bottomLayout) {
            top.linkTo(centerLayout.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, horizontalAlignment = Alignment.CenterHorizontally) {
            DefaultButton(
                onClick = { eventHandler.obtainEvent(RegistrationEvent.Submit) },
                text = stringResource(R.string.registration_action)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Row {
                AuthorizationText(text = stringResource(R.string.already_have_account_question))

                Spacer(modifier = Modifier.size(4.dp))

                AuthorizationTextClickable(
                    text = stringResource(R.string.login_action),
                    onClick = { navigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}