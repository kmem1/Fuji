package com.clownteam.fuji.ui.navigation

import android.os.Bundle
import androidx.compose.animation.*
import com.google.accompanist.navigation.animation.composable
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationController(
    router: Router? = null,
    startDestination: String,
    screens: List<NavigationControllerScreen> = emptyList()
) {
    val navigation = rememberAnimatedNavController()

    AnimatedNavHost(navController = navigation, startDestination = startDestination) {
        screens.forEach { screen ->
            composable(
                screen.route,
                enterTransition = screen.enterTransition,
                exitTransition = screen.exitTransition,
                popEnterTransition = screen.popEnterTransition,
                popExitTransition = screen.popExitTransition
            ) {
                screen.content.invoke(
                    navigation,
                    router,
                    navigation.previousBackStackEntry?.arguments
                )
            }
        }
    }
}

data class NavigationControllerScreen @OptIn(ExperimentalAnimationApi::class) constructor(
    val route: String,
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = { defaultEnterTransition() },
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = { defaultExitTransition() },
    val popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    val popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    val content: @Composable (NavController, Router?, Bundle?) -> Unit
)

private fun defaultEnterTransition(): EnterTransition = slideInHorizontally { 1000 }

private fun defaultExitTransition(): ExitTransition = slideOutHorizontally { 1000 }

