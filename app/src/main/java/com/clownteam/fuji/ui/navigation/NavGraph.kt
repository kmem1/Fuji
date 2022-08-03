package com.clownteam.fuji.ui.navigation

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import coil.ImageLoader
import com.clownteam.fuji.auth.TokenManagerImpl
import com.clownteam.fuji.auth.UserDataManagerImpl
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.fuji.ui.navigation.screens.archive.ArchiveContainer
import com.clownteam.fuji.ui.navigation.screens.course.CourseScreen
import com.clownteam.fuji.ui.navigation.screens.profile.ProfileContainer
import com.clownteam.fuji.ui.navigation.screens.search.SearchScreen
import com.clownteam.ui_authorization.login.LoginScreen
import com.clownteam.ui_authorization.login.LoginViewModel
import com.clownteam.ui_authorization.registration.RegistrationScreen
import com.clownteam.ui_authorization.registration.RegistrationViewModel
import com.clownteam.ui_authorization.restore_password.RestorePasswordScreen
import com.clownteam.ui_authorization.restore_password.RestorePasswordViewModel
import com.clownteam.ui_collectionaction.add_to_collection.AddToCollectionScreen
import com.clownteam.ui_collectionaction.add_to_collection.AddToCollectionScreenViewModel
import com.clownteam.ui_collectionaction.create_collection.CreateCollectionScreen
import com.clownteam.ui_collectionaction.create_collection.CreateCollectionViewModel
import com.clownteam.ui_courselist.ui.CourseList
import com.clownteam.ui_courselist.ui.CourseListViewModel
import com.clownteam.ui_coursepassing.course_lessons.CourseLessons
import com.clownteam.ui_coursepassing.course_lessons.CourseLessonsViewModel
import com.clownteam.ui_coursepassing.course_modules.CourseModules
import com.clownteam.ui_coursepassing.course_modules.CourseModulesViewModel
import com.clownteam.ui_coursepassing.course_steps.CourseSteps
import com.clownteam.ui_coursepassing.course_steps.CourseStepsViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.gson.Gson

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    imageLoader: ImageLoader,
    showBottomBar: (Boolean) -> Unit
) {
    val tokenManager = TokenManagerImpl.create()

    val startDestination = if (tokenManager.getToken() == null) {
        Route.LoginRoute.route
    } else {
        BottomNavItem.Home.route
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { defaultEnterTransition() },
        exitTransition = { defaultExitTransition() },
        popEnterTransition = { defaultEnterTransition() },
        popExitTransition = { defaultExitTransition() }
    ) {
        // ********* Bottom Bar navigation *********

        bottomItemComposable(BottomNavItem.Home.route) {
            showBottomBar(true)
            val viewModel: CourseListViewModel = hiltViewModel()
            CourseList(
                state = viewModel.state.value,
                eventHandler = viewModel,
                viewModel = viewModel,
                navigateToDetailScreen = { courseId ->
                    navController.navigate(Route.CourseRoute.getRouteWithArgument(courseId))
                },
                navigateToAddToCollection = { courseId ->
                    navController.navigate(Route.AddToCollectionRoute.getRouteWithArgument(courseId))
                },
                imageLoader = imageLoader,
                navigateToLogin = {
                    navController.navigate(Route.LoginRoute.route) {
                        popUpTo(BottomNavItem.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        bottomItemComposable(BottomNavItem.Search.route) {
            showBottomBar(true)
            SearchScreen()
        }

        bottomItemComposable(BottomNavItem.Archive.route) {
            showBottomBar(true)
            ArchiveContainer(
                externalRouter = createExternalRouter { route, params, builder ->
                    navController.navigate(route, params, builder)
                },
                imageLoader = imageLoader
            )
        }

        bottomItemComposable(BottomNavItem.Profile.route) {
            showBottomBar(true)
            ProfileContainer(
                createExternalRouter { route, params, builder ->
                    navController.navigate(route, params, builder)
                },
                showBottomBar
            )
        }

        // ********* Login navigation *********

        composable(Route.LoginRoute.route,
            enterTransition = {
                val offsetX = if (initialState.destination.route == Route.RegistrationRoute.route) {
                    -1000
                } else {
                    1000
                }
                slideInHorizontally(initialOffsetX = { offsetX })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 })
            }
        ) {
            showBottomBar(false)
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                state = viewModel.state,
                eventHandler = viewModel,
                viewModel = viewModel,
                navigateToRegistration = { navController.navigate(Route.RegistrationRoute.route) },
                navigateToRestorePassword = { navController.navigate(Route.RestorePasswordRoute.route) },
                onSuccessLogin = { access, refresh, username ->
                    val userDataManager = UserDataManagerImpl()

                    tokenManager.setToken(access)
                    tokenManager.setRefresh(refresh)

                    userDataManager.setUserPath(username)

                    navController.navigate(BottomNavItem.Home.route)
                }
            )
        }

        composable(Route.RegistrationRoute.route) {
            showBottomBar(false)
            val viewModel: RegistrationViewModel = hiltViewModel()
            RegistrationScreen(
                state = viewModel.state,
                eventHandler = viewModel,
                viewModel = viewModel,
                navigateToLogin = {
                    navController.navigate(
                        Route.LoginRoute.route,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(Route.LoginRoute.route, true).build()
                    )
                },
                onSuccessRegistration = {
                    navController.navigate(
                        Route.LoginRoute.route,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(Route.LoginRoute.route, true).build()
                    )
                }
            )
        }

        composable(Route.RestorePasswordRoute.route) {
            showBottomBar(false)
            val viewModel: RestorePasswordViewModel = hiltViewModel()
            RestorePasswordScreen(
                state = viewModel.state,
                eventHandler = viewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        // ********* Collection navigation *********

        composable(Route.AddToCollectionRoute.route) {
            showBottomBar(false)
            val viewModel: AddToCollectionScreenViewModel = hiltViewModel()
            AddToCollectionScreen(
                state = viewModel.state.value,
                eventHandler = viewModel,
                imageLoader = imageLoader,
                onBack = { navController.navigateUp() },
                navigateToCreateCollection = { courseId ->
                    navController.navigate(Route.CreateCollectionRoute.getRouteWithArgument(courseId))
                }
            )
        }

        composable(Route.CreateCollectionRoute.route) {
            showBottomBar(false)
            val viewModel: CreateCollectionViewModel = hiltViewModel()
            CreateCollectionScreen(
                state = viewModel.state.value,
                eventHandler = viewModel,
                viewModel = viewModel,
                onBack = { navController.navigateUp() },
                onSuccessCreate = {
                    navController.navigateUp()
                    navController.navigateUp()
                }
            )
        }

        // ********* Course navigation *********

        composable(
            route = Route.CourseRoute.route,
            arguments = Route.CourseRoute.arguments,
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            popEnterTransition = { slideInHorizontally { -1000 } }
        ) {
            showBottomBar(false)
            CourseScreen(
                imageLoader,
                onBack = { navController.popBackStack() },
                navigateToPassing = { courseId ->
                    navController.navigate(Route.CourseModulesRoute.getRouteWithArgument(courseId))
                }
            )
        }

        composable(
            route = Route.CourseModulesRoute.route,
            arguments = Route.CourseModulesRoute.arguments
        ) {
            showBottomBar(false)
            val viewModel: CourseModulesViewModel = hiltViewModel()
            CourseModules(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.popBackStack() },
                onModuleClick = { courseId, moduleId, moduleName ->
                    val args =
                        CourseLessonsViewModel.CourseLessonsArgs(courseId, moduleId, moduleName)
                    val argsJson = Uri.encode(Gson().toJson(args))
                    navController.navigate(Route.CourseLessonsRoute.getRouteWithArgument(argsJson))
                }
            )
        }

        composable(
            route = Route.CourseLessonsRoute.route,
            arguments = Route.CourseLessonsRoute.arguments
        ) {
            showBottomBar(false)
            val viewModel: CourseLessonsViewModel = hiltViewModel()
            CourseLessons(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.popBackStack() },
                onLessonClick = { courseId, moduleId, lessonId, lessonName, stepId ->
                    val args = CourseStepsViewModel.CourseStepsArgs(
                        courseId, moduleId, lessonId, lessonName, stepId
                    )

                    val argsJson = Uri.encode(Gson().toJson(args))
                    navController.navigate(Route.CourseStepsRoute.getRouteWithArgument(argsJson))
                }
            )
        }

        composable(
            route = Route.CourseStepsRoute.route,
            arguments = Route.CourseStepsRoute.arguments
        ) {
            showBottomBar(false)
            val viewModel: CourseStepsViewModel = hiltViewModel()
            CourseSteps(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.bottomItemComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = { bottomItemEnterTransition() },
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = { bottomItemExitTransition() },
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route,
        arguments,
        deepLinks,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        content
    )
}

private fun defaultEnterTransition(): EnterTransition = slideInHorizontally { 1000 }

private fun defaultExitTransition(): ExitTransition = slideOutHorizontally { 1000 }

private fun bottomItemEnterTransition(): EnterTransition = fadeIn(animationSpec = tween(700))

private fun bottomItemExitTransition(): ExitTransition = fadeOut(animationSpec = tween(700))