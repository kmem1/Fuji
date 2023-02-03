package com.clownteam.ui_authorization.registration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    object SuccessRegistrationRoute : NavigationRoute()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen(
    state: RegistrationState,
    eventHandler: EventHandler<RegistrationEvent>,
    navigateToLogin: () -> Unit = {},
    onSuccessRegistration: () -> Unit = {}
) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    val kc = LocalSoftwareKeyboardController.current

    val onRegisterButtonClick = {
        eventHandler.obtainEvent(RegistrationEvent.Submit)
        kc?.hide()
    }

    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Login -> {
                    navigateToLogin()
                }

                NavigationRoute.SuccessRegistrationRoute -> {
                    onSuccessRegistration()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.message) {
        state.message?.showToast(context)
        eventHandler.obtainEvent(RegistrationEvent.MessageShown)
    }

    if (state.isSuccessRegistration) {
        navigationRoute = NavigationRoute.SuccessRegistrationRoute
    }

    if (state.isLoading) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondary
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
            val login = state.login.collectAsState(initial = "")
            AuthorizationTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                value = { login.value },
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.LoginChanged(it)) },
                hint = stringResource(R.string.login_hint),
                isError = state.loginError != null,
                errorText = state.loginError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.size(18.dp))

            val email = state.email.collectAsState(initial = "")
            AuthorizationTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                value = { email.value },
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.EmailChanged(it)) },
                hint = stringResource(R.string.email_hint),
                isError = state.emailError != null,
                errorText = state.emailError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.size(18.dp))

            val password = state.password.collectAsState(initial = "")
            AuthorizationTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                value = { password.value },
                onValueChange = { eventHandler.obtainEvent(RegistrationEvent.PasswordChanged(it)) },
                hint = stringResource(R.string.password_hint),
                isError = state.passwordError != null,
                errorText = state.passwordError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.size(18.dp))

            val repeatedPassword = state.repeatedPassword.collectAsState(initial = "")
            AuthorizationTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                value = { repeatedPassword.value },
                onValueChange = {
                    eventHandler.obtainEvent(
                        RegistrationEvent.RepeatedPasswordChanged(
                            it
                        )
                    )
                },
                hint = stringResource(R.string.repeated_password_hint),
                isError = state.repeatedPasswordError != null,
                errorText = state.repeatedPasswordError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onRegisterButtonClick()
                    }
                ),
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Column(modifier = Modifier.constrainAs(bottomLayout) {
            top.linkTo(centerLayout.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.size(16.dp))

            DefaultButton(
                onClick = { onRegisterButtonClick() },
                text = stringResource(R.string.registration_action),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.size(16.dp))

            Row {
                AuthorizationText(text = stringResource(R.string.already_have_account_question))

                Spacer(modifier = Modifier.size(4.dp))

                AuthorizationTextClickable(
                    text = stringResource(R.string.login_action),
                    onClick = { navigationRoute = NavigationRoute.Login }
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}