package com.clownteam.fuji.ui.navigation

import android.os.Bundle
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
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
                exitTransition = screen.exitTransition
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
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    val content: @Composable (NavController, Router?, Bundle?) -> Unit
)