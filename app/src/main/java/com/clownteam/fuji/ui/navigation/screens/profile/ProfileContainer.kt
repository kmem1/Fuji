package com.clownteam.fuji.ui.navigation.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.NavigationController
import com.clownteam.fuji.ui.navigation.NavigationControllerScreen
import com.clownteam.fuji.ui.navigation.Route
import com.clownteam.fuji.ui.navigation.Router
import com.clownteam.ui_profile.change_password.ChangePasswordScreen
import com.clownteam.ui_profile.change_password.ChangePasswordViewModel
import com.clownteam.ui_profile.change_profile.ChangeProfileScreen
import com.clownteam.ui_profile.change_profile.ChangeProfileViewModel
import com.clownteam.ui_profile.profile.ProfileScreen
import com.clownteam.ui_profile.profile.ProfileViewModel
import com.clownteam.ui_profile.settings.SettingsScreen
import com.clownteam.ui_profile.settings.SettingsViewModel

@Composable
fun ProfileContainer(
    externalRouter: Router,
    showBottomBar: (Boolean) -> Unit,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit,
    navigateToCourse: (String) -> Unit,
    navigateToCollection: (String) -> Unit,
    navigateToSettings: () -> Unit
) {
    NavigationController(
        startDestination = Route.ProfileRoute.route,
        router = externalRouter,
        screens = listOf(
            createProfileNavigationControllerScreen(
                showBottomBar = showBottomBar,
                imageLoader = imageLoader,
                navigateToLogin = navigateToLogin,
                navigateToCollection = navigateToCollection,
                navigateToCourse = navigateToCourse,
                navigateToSettings = navigateToSettings
            ),
            createSettingsNavigationControllerScreen(
                showBottomBar = showBottomBar,
                imageLoader = imageLoader,
                navigateToLogin = navigateToLogin
            ),
            createChangeProfileNavigationControllerScreen(
                showBottomBar = showBottomBar,
                imageLoader = imageLoader
            ),
            createChangePasswordNavigationControllerScreen(
                showBottomBar = showBottomBar
            )
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createProfileNavigationControllerScreen(
    showBottomBar: (Boolean) -> Unit,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit,
    navigateToCollection: (String) -> Unit,
    navigateToCourse: (String) -> Unit,
    navigateToSettings: () -> Unit
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ProfileRoute.route
    ) { navController, _, _ ->
        showBottomBar(true)
        OpenProfileScreen(
            navController = navController,
            imageLoader = imageLoader,
            navigateToLogin = navigateToLogin,
            navigateToCollection = navigateToCollection,
            navigateToCourse = navigateToCourse,
            navigateToSettings = {
                navController.navigate(Route.SettingsRoute.route)
            }
        )
    }
}

@Composable
private fun OpenProfileScreen(
    navController: NavController,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit,
    navigateToCollection: (String) -> Unit,
    navigateToCourse: (String) -> Unit,
    navigateToSettings: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateToLogin = navigateToLogin,
        imageLoader = imageLoader,
        navigateToCollection = navigateToCollection,
        navigateToCourse = navigateToCourse,
        navigateToSettings = navigateToSettings
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createSettingsNavigationControllerScreen(
    showBottomBar: (Boolean) -> Unit,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.SettingsRoute.route
    ) { navController, _, _ ->
        showBottomBar(true)
        OpenSettingsScreen(
            navController = navController,
            navigateToLogin = navigateToLogin
        )
    }
}

@Composable
private fun OpenSettingsScreen(
    navController: NavController,
    navigateToLogin: () -> Unit
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    SettingsScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateToLogin = navigateToLogin,
        navigateToChangeProfile = {
            navController.navigate(Route.ChangeProfileRoute.route)
        },
        navigateToChangePassword = {
            navController.navigate(Route.ChangePasswordRoute.route)
        },
        navigateBack = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createChangeProfileNavigationControllerScreen(
    showBottomBar: (Boolean) -> Unit,
    imageLoader: ImageLoader
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ChangeProfileRoute.route
    ) { navController, _, _ ->
        showBottomBar(true)
        OpenChangeProfileScreen(navController = navController, imageLoader = imageLoader)
    }
}

@Composable
private fun OpenChangeProfileScreen(
    navController: NavController,
    imageLoader: ImageLoader
) {
    val viewModel: ChangeProfileViewModel = hiltViewModel()
    ChangeProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        imageLoader = imageLoader,
        navigateBack = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createChangePasswordNavigationControllerScreen(
    showBottomBar: (Boolean) -> Unit
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ChangePasswordRoute.route
    ) { navController, _, _ ->
        showBottomBar(true)
        OpenChangePasswordScreen(navController = navController)
    }
}

@Composable
private fun OpenChangePasswordScreen(
    navController: NavController
) {
    val viewModel: ChangePasswordViewModel = hiltViewModel()
    ChangePasswordScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateBack = { navController.navigateUp() }
    )
}