package com.clownteam.fuji.ui.navigation.screens.profile

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.clownteam.fuji.api.FujiApi
import com.clownteam.fuji.api.token.TokenService
import com.clownteam.fuji.auth.TokenManagerImpl
import com.clownteam.fuji.auth.UserDataManagerImpl
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
import com.clownteam.ui_profile.ProfileScreen
import com.clownteam.ui_profile.ProfileViewModel

private const val PROFILE_SCREEN_TOKEN_KEY = "token_key"

@Composable
fun ProfileContainer(externalRouter: Router) {
    val tokenManager = TokenManagerImpl(FujiApi.createService(TokenService::class.java))

    val startDestination =
        if (tokenManager.getToken() == null) Route.LoginRoute.route else Route.ProfileRoute.route

    Log.d("Kmem", "$startDestination ${tokenManager.getToken()}")

    NavigationController(
        startDestination = startDestination,
        router = externalRouter,
        screens = listOf(
            createProfileNavigationControllerScreen(),
            createLoginNavigationControllerScreen(),
            createRegistrationNavigationControllerScreen(),
            createRestorePasswordNavigationControllerScreen()
        )
    )
}


@OptIn(ExperimentalAnimationApi::class)
private fun createProfileNavigationControllerScreen(): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ProfileRoute.route
    ) { navController, _, bundle ->
        val token = bundle?.getString(PROFILE_SCREEN_TOKEN_KEY)

        Log.d("Kmem", "bundle: $bundle bundleToken: $token")

        OpenProfileScreen(navController)
    }
}

@Composable
private fun OpenProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        viewModel = viewModel,
        navigateToLogin = { navController.navigate(Route.LoginRoute.route) }
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
    ) { navController, _, _ ->
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
        navigateToRestorePassword = { navController.navigate(Route.RestorePasswordRoute.route) },
        onSuccessLogin = { access, refresh, username ->
            val tokenManager = TokenManagerImpl(FujiApi.createService(TokenService::class.java))
            val userDataManager = UserDataManagerImpl()

            tokenManager.setToken(access)
            tokenManager.setRefresh(refresh)

            Log.d("kmem", "setUsername: $username")
            userDataManager.setUserPath(username)

            navController.navigate(Route.ProfileRoute.route)
        }
    )
}

private fun createRegistrationNavigationControllerScreen(): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.RegistrationRoute.route
    ) { navController, _, _ ->
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
        onSuccessRegistration = {
            navController.navigate(
                Route.LoginRoute.route,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(Route.LoginRoute.route, true).build()
            )
        }
    )
}

private fun createRestorePasswordNavigationControllerScreen(): NavigationControllerScreen {
    return NavigationControllerScreen(route = Route.RestorePasswordRoute.route) { navController, _, _ ->
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