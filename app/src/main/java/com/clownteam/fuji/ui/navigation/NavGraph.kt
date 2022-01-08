package com.clownteam.fuji.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.ui_courselist.ui.CourseList
import com.clownteam.ui_courselist.ui.CourseListViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SetupNavGraph(navController: NavHostController, imageLoader: ImageLoader) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            val viewModel: CourseListViewModel = hiltViewModel()
            CourseList(
                state = viewModel.state.value,
                eventHandler = viewModel,
                navigateToDetailScreen = { _ -> },
                imageLoader = imageLoader
            )
        }

        composable(BottomNavItem.Search.route) {
            Scaffold {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Search"
                    )
                }
            }
        }

        composable(BottomNavItem.Profile.route) {
            Scaffold {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Profile"
                    )
                }
            }
        }
    }
}
