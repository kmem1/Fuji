package com.clownteam.ui_profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.core.domain.EventHandler

private sealed class NavigationRoute {
    object Login : NavigationRoute()
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    eventHandler: EventHandler<ProfileEvent>,
    navigateToLogin: () -> Unit = {}
) {
    var navigationRoute by remember { mutableStateOf<NavigationRoute?>(null) }

    LaunchedEffect(key1 = navigationRoute) {
        navigationRoute?.let {
            when (it) {
                NavigationRoute.Login -> {
                    navigateToLogin()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isNetworkError) {
            Column(modifier = Modifier.fillMaxSize()) {
                DefaultButton(
                    text = "Retry",
                    onClick = { eventHandler.obtainEvent(ProfileEvent.GetProfile) })
            }
        } else {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (state.profileData == null) {
                navigationRoute = NavigationRoute.Login
            } else {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (username, exitButton) = createRefs()

                    Text(
                        text = state.profileData.username,
                        modifier = Modifier.constrainAs(username) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        })

                    DefaultButton(
                        text = "Выйти",
                        onClick = { eventHandler.obtainEvent(ProfileEvent.SignOut) },
                        modifier = Modifier
                            .constrainAs(exitButton) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}