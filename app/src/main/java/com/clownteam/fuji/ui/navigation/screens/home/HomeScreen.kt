package com.clownteam.fuji.ui.navigation.screens.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.clownteam.ui_courselist.ui.CourseList
import com.clownteam.ui_courselist.ui.CourseListViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(
    navigateToCourse: (String) -> Unit,
    navigateToAddToCollection: (String) -> Unit,
    imageLoader: ImageLoader
) {
    val viewModel: CourseListViewModel = hiltViewModel()
    CourseList(
        state = viewModel.state.value,
        eventHandler = viewModel,
        navigateToDetailScreen = navigateToCourse,
        navigateToAddToCollection = navigateToAddToCollection,
        imageLoader = imageLoader
    )
}