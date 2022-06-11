package com.clownteam.fuji.ui.navigation.screens.archive

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.NavigationController
import com.clownteam.fuji.ui.navigation.NavigationControllerScreen
import com.clownteam.fuji.ui.navigation.Route
import com.clownteam.fuji.ui.navigation.Router
import com.clownteam.ui_collectiondetailed.ui.CollectionDetailed
import com.clownteam.ui_collectiondetailed.ui.CollectionDetailedViewModel
import com.clownteam.ui_collectionlist.CollectionList
import com.clownteam.ui_collectionlist.CollectionListViewModel

private const val PROFILE_SCREEN_TOKEN_KEY = "token_key"

@Composable
fun ArchiveContainer(externalRouter: Router, imageLoader: ImageLoader) {
    NavigationController(
        startDestination = Route.ArchiveRoute.route,
        router = externalRouter,
        screens = listOf(
            createArchiveNavigationControllerScreen(imageLoader),
            createCollectionDetailedNavigationControllerScreen(imageLoader)
        )
    )
}


@OptIn(ExperimentalAnimationApi::class)
private fun createArchiveNavigationControllerScreen(imageLoader: ImageLoader): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.ArchiveRoute.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 })
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
    ) { navController, _, bundle ->
        OpenArchiveScreen(navController, imageLoader)
    }
}

@Composable
private fun OpenArchiveScreen(
    navController: NavController,
    imageLoader: ImageLoader
) {
    val viewModel: CollectionListViewModel = hiltViewModel()
    CollectionList(
        state = viewModel.state.value,
        eventHandler = viewModel,
        imageLoader = imageLoader,
        navigateToDetailed = { id ->
            val route = Route.CourseCollectionRoute.getRouteWithArgument(id)
            navController.navigate(route)
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun createCollectionDetailedNavigationControllerScreen(imageLoader: ImageLoader): NavigationControllerScreen {
    return NavigationControllerScreen(
        route = Route.CourseCollectionRoute.route
    ) { navController, _, bundle ->
        OpenCourseCollectionScreen(navController, imageLoader)
    }
}

@Composable
private fun OpenCourseCollectionScreen(
    navController: NavController,
    imageLoader: ImageLoader
) {
    val viewModel: CollectionDetailedViewModel = hiltViewModel()
    CollectionDetailed(
        state = viewModel.state.value,
        eventHandler = viewModel,
        imageLoader = imageLoader,
        onBackPressed = { navController.popBackStack() },
        navigateToLogin = {},
        navigateToDetailed = {}
    )
}