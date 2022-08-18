package com.clownteam.fuji.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.SetupNavGraph
import com.clownteam.fuji.ui.navigation.bottom_navigation.AppBottomNavigation
import com.clownteam.fuji.ui.theme.FujiTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }

        super.onCreate(savedInstanceState)

        setContent {
            FujiTheme {
                val navController = rememberAnimatedNavController()
                var bottomBarState by remember { mutableStateOf(true) }
//                var animateBottomBar by remember { mutableStateOf(false) }
                val isImeVisible = WindowInsets.isImeVisible
                Scaffold(
                    bottomBar = {
//                        AnimatedVisibility(
//                            visible = bottomBarState && !isImeVisible,
//                            enter = fadeIn(),
//                            exit = fadeOut()
//                        ) {
//                            AppBottomNavigation(navController)
//                        }
                        if (bottomBarState && !isImeVisible) {
                            AppBottomNavigation(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        SetupNavGraph(navController, imageLoader) { value ->
                            bottomBarState = value
                        }
                    }
                }
            }
        }
    }
}