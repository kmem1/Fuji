package com.clownteam.ui_profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.core.domain.EventHandler
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    state: ProfileState,
    eventHandler: EventHandler<ProfileEvent>,
    viewModel: ProfileViewModel,
    navigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current

    Log.d("Kmem", "ProfileScreen")

    LaunchedEffect(key1 = context) {
        eventHandler.obtainEvent(ProfileEvent.GetProfile)

        viewModel.events.collectLatest { event ->
            Log.d("Kmem", "$event")
            when (event) {
                ProfileViewModel.ProfileViewModelEvent.UnauthorizedEvent -> {
                    navigateToLogin()
                }

                ProfileViewModel.ProfileViewModelEvent.NavigateToLoginEvent -> {
                    navigateToLogin()
                }
            }
        }
    }

    if (state.isNetworkError) {
        Column(modifier = Modifier.fillMaxSize()) {
            DefaultButton(
                text = "Retry",
                onClick = { eventHandler.obtainEvent(ProfileEvent.GetProfile) })
        }
    } else {
        if (state.isLoading) {
            LinearProgressIndicator()
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (username, exitButton) = createRefs()

            Text(text = state.username, modifier = Modifier.constrainAs(username) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })

            DefaultButton(
                text = "Выйти",
                onClick = { eventHandler.obtainEvent(ProfileEvent.SignOut) },
                modifier = Modifier.constrainAs(exitButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.padding(bottom = 24.dp)
            )
        }
    }
}