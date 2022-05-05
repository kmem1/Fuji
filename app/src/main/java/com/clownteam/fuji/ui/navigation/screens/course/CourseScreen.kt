package com.clownteam.fuji.ui.navigation.screens.course

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.clownteam.ui_coursedetailed.ui.CourseDetailed
import com.clownteam.ui_coursedetailed.ui.CourseDetailedViewModel

@Composable
fun CourseScreen(imageLoader: ImageLoader, onBack: () -> Unit) {
    val viewModel: CourseDetailedViewModel = hiltViewModel()
    CourseDetailed(
        viewModel.state.value,
        viewModel,
        imageLoader,
        onBack
    )
}