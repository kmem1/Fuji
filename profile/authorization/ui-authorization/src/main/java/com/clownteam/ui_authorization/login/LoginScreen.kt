package com.clownteam.ui_authorization.login

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
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
    object Registration : NavigationRoute()
    object RestorePassword : NavigationRoute()

    class SuccessLoginRoute(
        val accessToken: String,
        val refreshToken: String,
        val userPath: String
    ) : NavigationRoute()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    state: LoginState,
    eventHandler: EventHandler<LoginEvent>,
    navigateToRegistration: () -> Unit = {},
    navigateToRestorePassword: () -> Unit = {},
    onSuccessLogin: (access: String, refresh: String, username: String) -> Unit
) {
    val context = LocalContext.current

    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Registration -> {
                    navigateToRegistration()
                }

                NavigationRoute.RestorePassword -> {
                    navigateToRestorePassword()
                }

                is NavigationRoute.SuccessLoginRoute -> {
                    onSuccessLogin(it.accessToken, it.refreshToken, it.userPath)
                }
            }
        }
    }

    LaunchedEffect(key1 = state.errorMessage) {
        state.errorMessage?.showToast(context)
        eventHandler.obtainEvent(LoginEvent.FailMessageShown)
    }

    if (state.loginResult != null) {
        navigationRoute = NavigationRoute.SuccessLoginRoute(
            state.loginResult.access,
            state.loginResult.refresh,
            state.loginResult.userPath
        )
    }

    if (state.isLoading) {
        LinearProgressIndicator(color = MaterialTheme.colors.secondary)
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
            text = stringResource(R.string.login_title),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                value = state.email,
                onValueChange = { eventHandler.obtainEvent(LoginEvent.EmailChanged(it)) },
                hint = stringResource(R.string.email_hint),
                isError = state.emailError != null,
                errorText = state.emailError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
            )

            Spacer(modifier = Modifier.size(18.dp))

            AuthorizationTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .focusRequester(focusRequester),
                value = state.password,
                onValueChange = { eventHandler.obtainEvent(LoginEvent.PasswordChanged(it)) },
                hint = stringResource(R.string.password_hint),
                isError = state.passwordError != null,
                errorText = state.passwordError?.asString(context) ?: "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation()
            )

            AuthorizationTextClickable(
                text = stringResource(R.string.restore_password_question),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp, end = 32.dp),
                onClick = {
                    if (!state.isLoading) {
                        navigationRoute = NavigationRoute.RestorePassword
                    }
                }
            )
        }

        Column(modifier = Modifier.constrainAs(bottomLayout) {
            top.linkTo(centerLayout.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, horizontalAlignment = Alignment.CenterHorizontally) {
            DefaultButton(
                onClick = { eventHandler.obtainEvent(LoginEvent.Submit) },
                text = stringResource(R.string.login_action)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Row {
                AuthorizationText(text = stringResource(R.string.create_account_question))

                Spacer(modifier = Modifier.size(4.dp))

                AuthorizationTextClickable(
                    text = stringResource(R.string.registration_action),
                    onClick = {
                        if (!state.isLoading) {
                            navigationRoute = NavigationRoute.Registration
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}