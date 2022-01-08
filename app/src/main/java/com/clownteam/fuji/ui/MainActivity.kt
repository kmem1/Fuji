package com.clownteam.fuji.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.SetupNavGraph
import com.clownteam.fuji.ui.navigation.bottom_navigation.AppBottomNavigation
import com.clownteam.fuji.ui.theme.FujiTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepVisibleCondition {
                viewModel.isLoading.value
            }
        }

        setContent {
            FujiTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = { AppBottomNavigation(navController) }) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        SetupNavGraph(navController, imageLoader)
                    }
                }
            }
        }
    }
}