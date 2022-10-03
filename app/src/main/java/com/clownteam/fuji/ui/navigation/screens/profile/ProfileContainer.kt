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
import com.clownteam.ui_profile.ProfileScreen
import com.clownteam.ui_profile.ProfileViewModel

@Composable
fun ProfileContainer(
    externalRouter: Router,
    showBottomBar: (Boolean) -> Unit,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit
) {
    NavigationController(
        startDestination = Route.ProfileRoute.route,
        router = externalRouter,
        screens = listOf(
            createProfileNavigationControllerScreen(showBottomBar, imageLoader, navigateToLogin)
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createProfileNavigationControllerScreen(
    showBottomBar: (Boolean) -> Unit,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ProfileRoute.route
    ) { navController, _, _ ->
        showBottomBar(true)
        OpenProfileScreen(navController, imageLoader, navigateToLogin)
    }
}

@Composable
private fun OpenProfileScreen(
    navController: NavController,
    imageLoader: ImageLoader,
    navigateToLogin: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateToLogin = navigateToLogin,
        imageLoader = imageLoader
    )
}