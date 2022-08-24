package com.clownteam.ui_collectionaction.create_collection

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_collectionaction.R

private sealed class NavigationRoute {
    object Login : NavigationRoute()
    object SuccessCreationRoute : NavigationRoute()
}

@Composable
fun CreateCollectionScreen(
    state: CreateCollectionState,
    eventHandler: EventHandler<CreateCollectionEvent>,
    onBack: () -> Unit,
    onSuccessCreate: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Login -> {
                    navigateToLogin()
                }

                NavigationRoute.SuccessCreationRoute -> {
                    onSuccessCreate()
                }
            }
        }
    }

    if (state.isUnauthorized) {
        navigationRoute = NavigationRoute.Login
    }

    if (state.isSuccess) {
        navigationRoute = NavigationRoute.SuccessCreationRoute
    }

    if (state.errorMessage != null) {
        state.errorMessage.showToast(context)
        eventHandler.obtainEvent(CreateCollectionEvent.ErrorMessageShown)
    }

    if (state.isLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }

    ConstraintLayout(modifier = Modifier.imePadding().fillMaxSize()) {
        val (header, label, textField, createButton) = createRefs()

        Box(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            DefaultHeader(
                stringResource(R.string.create_collection_title_text),
                onArrowClick = { onBack() }
            )
        }

        Text(
            "Введите название новой подборки",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(label) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(textField.top)
                }
                .padding(bottom = 75.dp)
        )

        var isTextFieldFocused by remember { mutableStateOf(false) }

        TextField(
            value = state.collectionTitle,
            onValueChange = { eventHandler.obtainEvent(CreateCollectionEvent.TitleChanged(it)) },
            placeholder = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val animatedColor =
                        animateColorAsState(if (isTextFieldFocused) Color.Gray else Color.White)

                    Text(
                        "Название",
                        style = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = animatedColor.value
                    )
                }
            },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .constrainAs(textField) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .onFocusChanged { isTextFieldFocused = it.isFocused },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent, cursorColor = Color.White),
            singleLine = true
        )

        DefaultButton(
            text = stringResource(R.string.create),
            onClick = {
                eventHandler.obtainEvent(CreateCollectionEvent.CreateCollection)
            },
            modifier = Modifier.constrainAs(createButton) {
                top.linkTo(textField.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            enabled = state.collectionTitle.isNotEmpty()
        )
    }
}


