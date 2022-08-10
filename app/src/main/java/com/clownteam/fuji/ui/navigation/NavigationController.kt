package com.clownteam.fuji.ui.navigation

import android.os.Bundle
import androidx.compose.animation.*
import com.google.accompanist.navigation.animation.composable
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.clownteam.fuji.ui.navigation.animation.defaultEnterTransition
import com.clownteam.fuji.ui.navigation.animation.defaultExitTransition
import com.clownteam.fuji.ui.navigation.animation.defaultPopEnterTransition
import com.clownteam.fuji.ui.navigation.animation.defaultPopExitTransition
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
                arguments = screen.arguments,
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
    val arguments: List<NamedNavArgument> = emptyList(),
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = { defaultEnterTransition() },
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = { defaultExitTransition() },
    val popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = { defaultPopEnterTransition() },
    val popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = { defaultPopExitTransition() },
    val content: @Composable (NavController, Router?, Bundle?) -> Unit
)
