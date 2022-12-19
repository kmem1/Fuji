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
import com.clownteam.ui_profile.all_collections.AllProfileCollectionsScreen
import com.clownteam.ui_profile.all_collections.AllProfileCollectionsViewModel
import com.clownteam.ui_profile.all_courses.AllProfileCoursesScreen
import com.clownteam.ui_profile.all_courses.AllProfileCoursesViewModel
import com.clownteam.ui_profile.change_password.ChangePasswordScreen
import com.clownteam.ui_profile.change_password.ChangePasswordViewModel
import com.clownteam.ui_profile.change_profile.ChangeProfileScreen
import com.clownteam.ui_profile.change_profile.ChangeProfileViewModel
import com.clownteam.ui_profile.profile.ProfileScreen
import com.clownteam.ui_profile.profile.ProfileViewModel
import com.clownteam.ui_profile.settings.SettingsScreen
import com.clownteam.ui_profile.settings.SettingsViewModel

@Composable
fun ProfileContainer(params: ProfileContainerParams) {
    NavigationController(
        startDestination = Route.ProfileRoute.route,
        router = params.externalRouter,
        screens = listOf(
            createProfileNavigationControllerScreen(params),
            createSettingsNavigationControllerScreen(params),
            createChangeProfileNavigationControllerScreen(
                showBottomBar = params.showBottomBar,
                imageLoader = params.imageLoader
            ),
            createChangePasswordNavigationControllerScreen(showBottomBar = params.showBottomBar),
            createAllProfileCoursesNavigationControllerScreen(params),
            createAllProfileCollectionsNavigationControllerScreen(params)
        )
    )
}

class ProfileContainerParams(
    val externalRouter: Router,
    val showBottomBar: (Boolean) -> Unit,
    val imageLoader: ImageLoader,
    val navigateToLogin: () -> Unit,
    val navigateToCourse: (String) -> Unit,
    val navigateToCollection: (String) -> Unit
)

@OptIn(ExperimentalAnimationApi::class)
private fun createProfileNavigationControllerScreen(params: ProfileContainerParams): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ProfileRoute.route
    ) { navController, _, _ ->
        params.showBottomBar(true)
        OpenProfileScreen(
            navController = navController,
            params = params
        )
    }
}

@Composable
private fun OpenProfileScreen(
    navController: NavController,
    params: ProfileContainerParams
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    ProfileScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        navigateToLogin = params.navigateToLogin,
        imageLoader = params.imageLoader,
        navigateToCollection = params.navigateToCollection,
        navigateToCourse = params.navigateToCourse,
        navigateToSettings = {
            navController.navigate(Route.SettingsRoute.route)
        },
        navigateToAllCourses = {
            navController.navigate(Route.AllProfileCoursesRoute.route)
        },
        navigateToAllCollections = {
            navController.navigate(Route.AllProfileCollectionsRoute.route)
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createSettingsNavigationControllerScreen(params: ProfileContainerParams): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.SettingsRoute.route
    ) { navController, _, _ ->
        params.showBottomBar(true)
        OpenSettingsScreen(
            navController = navController,
            navigateToLogin = params.navigateToLogin
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

@OptIn(ExperimentalAnimationApi::class)
private fun createAllProfileCoursesNavigationControllerScreen(
    params: ProfileContainerParams
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.AllProfileCoursesRoute.route
    ) { navController, _, _ ->
        params.showBottomBar(true)
        OpenAllProfileCoursesScreen(
            navController = navController,
            params.imageLoader,
            params.navigateToCourse
        )
    }
}

@Composable
private fun OpenAllProfileCoursesScreen(
    navController: NavController,
    imageLoader: ImageLoader,
    navigateToCourse: (String) -> Unit
) {
    val viewModel: AllProfileCoursesViewModel = hiltViewModel()
    AllProfileCoursesScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        imageLoader = imageLoader,
        navigateBack = { navController.navigateUp() },
        navigateToCourse = navigateToCourse
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createAllProfileCollectionsNavigationControllerScreen(
    params: ProfileContainerParams
): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.AllProfileCollectionsRoute.route
    ) { navController, _, _ ->
        params.showBottomBar(true)
        OpenAllProfileCollectionsScreen(
            navController = navController,
            params.imageLoader,
            params.navigateToCollection
        )
    }
}

@Composable
private fun OpenAllProfileCollectionsScreen(
    navController: NavController,
    imageLoader: ImageLoader,
    navigateToCollection: (String) -> Unit
) {
    val viewModel: AllProfileCollectionsViewModel = hiltViewModel()
    AllProfileCollectionsScreen(
        state = viewModel.state,
        eventHandler = viewModel,
        imageLoader = imageLoader,
        navigateBack = { navController.navigateUp() },
        navigateToCollection = navigateToCollection
    )
}