package com.clownteam.fuji.ui.navigation.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.clownteam.fuji.ui.navigation.NavigationController
import com.clownteam.fuji.ui.navigation.NavigationControllerScreen
import com.clownteam.fuji.ui.navigation.Route
import com.clownteam.fuji.ui.navigation.Router
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.ui_profile.ProfileScreen
import com.clownteam.ui_profile.ProfileViewModel

@Composable
fun ProfileContainer(
    externalRouter: Router,
    showBottomBar: (Boolean) -> Unit,
    navigateToLogin: () -> Unit
) {
    NavigationController(
        startDestination = Route.ProfileRoute.route,
        router = externalRouter,
        screens = listOf(
            createProfileNavigationControllerScreen(showBottomBar, navigateToLogin)
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createProfileNavigationControllerScreen(
    showBottomBar: (Boolean) -> Unit,
    navigateToLogin: () -> Unit
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ProfileRoute.route
    ) { navController, _, _ ->
        showBottomBar(true)
        OpenProfileScreen(navController, navigateToLogin)
    }
}

@Composable
private fun OpenProfileScreen(
    navController: NavController,
    navigateToLogin: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateToLogin = navigateToLogin
    )
}