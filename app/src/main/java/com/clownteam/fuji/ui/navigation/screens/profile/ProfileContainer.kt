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
fun ProfileContainer(externalRouter: Router, showBottomBar: (Boolean) -> Unit) {
    NavigationController(
        startDestination = Route.ProfileRoute.route,
        router = externalRouter,
        screens = listOf(
            createProfileNavigationControllerScreen(showBottomBar)
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createProfileNavigationControllerScreen(showBottomBar: (Boolean) -> Unit): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ProfileRoute.route
    ) { navController, externalRouter, _ ->
        showBottomBar(true)
        OpenProfileScreen(navController, externalRouter)
    }
}

@Composable
private fun OpenProfileScreen(navController: NavController, externalRouter: Router?) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        viewModel = viewModel,
        navigateToLogin = {
            externalRouter?.routeTo(Route.LoginRoute.route) {
                popUpTo(BottomNavItem.Home.route) {
                    inclusive = true
                }
            }
        }
    )
}