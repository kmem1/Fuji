package com.clownteam.fuji.ui.navigation.screens.course

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.clownteam.ui_coursedetailed.ui.CourseDetailed
import com.clownteam.ui_coursedetailed.ui.CourseDetailedViewModel

@Composable
fun CourseScreen(
    imageLoader: ImageLoader,
    onBack: () -> Unit,
    navigateToPassing: (String) -> Unit,
    navigateToAddToCollection: (courseId: String) -> Unit,
    navigateToLogin: () -> Unit
) {
    val viewModel: CourseDetailedViewModel = hiltViewModel()
    CourseDetailed(
        state = viewModel.state.value,
        eventHandler = viewModel,
        imageLoader = imageLoader,
        onBack = onBack,
        navigateToPassing = navigateToPassing,
        navigateToAddToCollection = navigateToAddToCollection,
        navigateToLogin = navigateToLogin
    )
}