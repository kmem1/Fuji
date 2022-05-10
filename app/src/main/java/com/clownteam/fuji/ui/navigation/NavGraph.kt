package com.clownteam.fuji.ui.navigation

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.fuji.ui.navigation.screens.HomeScreen
import com.clownteam.fuji.ui.navigation.screens.course.CourseScreen
import com.clownteam.fuji.ui.navigation.screens.home.HomeViewModel
import com.clownteam.fuji.ui.navigation.screens.search.SearchScreen
import com.clownteam.ui_authorization.login.LoginScreen
import com.clownteam.ui_authorization.login.LoginViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    imageLoader: ImageLoader,
    showBottomBar: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            showBottomBar(true)
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                navigateToCourse = { courseId ->
                    navController.navigate("${Route.CourseRoute.route}/$courseId")
                },
                imageLoader = imageLoader
            )
        }

        composable(
            route = Route.CourseRoute.route + "/{courseId}",
            arguments = Route.CourseRoute.arguments
        ) {
            showBottomBar(false)
            CourseScreen(
                imageLoader,
                onBack = { navController.popBackStack() }
            )
        }

        composable(BottomNavItem.Search.route) {
            SearchScreen()
        }

        composable(BottomNavItem.Profile.route) {
            val context = LocalContext.current
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                state = viewModel.state,
                eventHandler = viewModel,
                viewModel = viewModel,
                onSuccessLogin = { Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}
