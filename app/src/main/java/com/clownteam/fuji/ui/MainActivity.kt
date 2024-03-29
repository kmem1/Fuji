package com.clownteam.fuji.ui

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.SetupNavGraph
import com.clownteam.fuji.ui.navigation.bottom_navigation.AppBottomNavigation
import com.clownteam.fuji.ui.theme.FujiTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
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

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FujiTheme {
                val navController = rememberAnimatedNavController()
                var bottomBarState by remember { mutableStateOf(true) }
//                var animateBottomBar by remember { mutableStateOf(false) }
//                val isImeVisible = WindowInsets.isImeVisible
                Scaffold(
                    modifier = Modifier
                        .systemBarsPadding()
                        .navigationBarsPadding(),
                    bottomBar = {
//                        AnimatedVisibility(
//                            visible = bottomBarState && !isImeVisible,
//                            enter = fadeIn(),
//                            exit = fadeOut()
//                        ) {
//                            AppBottomNavigation(navController)
//                        }
                        if (bottomBarState) {
                            AppBottomNavigation(navController)
                        }
                    }
                ) { innerPadding ->
//                    val bottomBarPadding = if (isImeVisible) 0.dp else innerPadding.calculateBottomPadding()
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ContextWrapper(newBase?.setAppLocale("ru")))
    }
}

fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}