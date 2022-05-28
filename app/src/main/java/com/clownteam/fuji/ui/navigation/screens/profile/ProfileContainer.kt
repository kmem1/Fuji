package com.clownteam.fuji.ui.navigation.screens.profile

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.clownteam.fuji.ui.navigation.NavigationController
import com.clownteam.fuji.ui.navigation.Route
import com.clownteam.fuji.ui.navigation.Router
import com.clownteam.ui_authorization.login.LoginScreen
import com.clownteam.ui_authorization.login.LoginViewModel
import com.clownteam.ui_authorization.registration.RegistrationScreen
import com.clownteam.ui_authorization.registration.RegistrationViewModel
import com.clownteam.ui_authorization.restore_password.RestorePasswordScreen
import com.clownteam.ui_authorization.restore_password.RestorePasswordViewModel

@Composable
fun ProfileContainer(externalRouter: Router) {
    NavigationController(
        startDestination = Route.LoginRoute.route,
        router = externalRouter,
        screens = listOf(
            Pair(Route.LoginRoute.route) { navController, _, _ ->
                OpenLoginScreen(navController)
            },

            Pair(Route.RegistrationRoute.route) { navController, _, _ ->
                OpenRegistrationScreen(navController)
            },

            Pair(Route.RestorePasswordRoute.route) { navController, _, _ ->
                OpenRestorePasswordScreen(navController)
            }
        )
    )
}

@Composable
private fun OpenLoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = hiltViewModel()
    LoginScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        viewModel = viewModel,
        navigateToRegistration = { navController.navigate(Route.RegistrationRoute.route) },
        navigateToRestorePassword = { navController.navigate(Route.RestorePasswordRoute.route) })
}

@Composable
private fun OpenRegistrationScreen(navController: NavController) {
    val viewModel: RegistrationViewModel = hiltViewModel()
    RegistrationScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        viewModel = viewModel,
        navigateToLogin = {
            navController.navigate(
                Route.LoginRoute.route,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(Route.LoginRoute.route, true).build()
            )
        },
        onSuccessRegistration = {}
    )
}

@Composable
private fun OpenRestorePasswordScreen(navController: NavController) {
    val viewModel: RestorePasswordViewModel = hiltViewModel()
    RestorePasswordScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateBack = { navController.popBackStack() }
    )
}