package com.clownteam.fuji.ui.navigation.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.clownteam.fuji.ui.navigation.NavigationController
import com.clownteam.fuji.ui.navigation.NavigationControllerScreen
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
            createLoginNavigationControllerScreen(),
            createRegistrationNavigationControllerScreen(),
            createRestorePasswordNavigationControllerScreen()
        )
    )
}


@OptIn(ExperimentalAnimationApi::class)
private fun createLoginNavigationControllerScreen(): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.LoginRoute.route,
        enterTransition = {
            val offsetX = if (initialState.destination.route == Route.RegistrationRoute.route) {
                -1000
            } else {
                1000
            }
            slideInHorizontally(initialOffsetX = { offsetX })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 })
        }
    ) { navController, router, bundle ->
        OpenLoginScreen(navController)
    }
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

private fun createRegistrationNavigationControllerScreen(): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.RegistrationRoute.route) { navController, router, bundle ->
        OpenRegistrationScreen(navController)
    }
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

private fun createRestorePasswordNavigationControllerScreen(): NavigationControllerScreen {
    return NavigationControllerScreen(route = Route.RestorePasswordRoute.route) { navController, router, bundle ->
        OpenRestorePasswordScreen(navController)
    }
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