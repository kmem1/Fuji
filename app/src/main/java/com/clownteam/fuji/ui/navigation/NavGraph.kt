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
import com.clownteam.fuji.ui.navigation.animation.defaultEnterTransition
import com.clownteam.fuji.ui.navigation.animation.defaultExitTransition
import com.clownteam.fuji.ui.navigation.animation.defaultPopEnterTransition
import com.clownteam.fuji.ui.navigation.animation.defaultPopExitTransition
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.fuji.ui.navigation.screens.course.CourseScreen
import com.clownteam.fuji.ui.navigation.screens.profile.ProfileContainer
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
import com.clownteam.ui_collectiondetailed.ui.CollectionDetailed
import com.clownteam.ui_collectiondetailed.ui.CollectionDetailedViewModel
import com.clownteam.ui_courselist.ui.CourseList
import com.clownteam.ui_courselist.ui.CourseListViewModel
import com.clownteam.ui_coursepassing.course_lessons.CourseLessons
import com.clownteam.ui_coursepassing.course_lessons.CourseLessonsViewModel
import com.clownteam.ui_coursepassing.course_modules.CourseModules
import com.clownteam.ui_coursepassing.course_modules.CourseModulesViewModel
import com.clownteam.ui_coursepassing.course_steps.CourseSteps
import com.clownteam.ui_coursepassing.course_steps.CourseStepsViewModel
import com.clownteam.ui_search.ui.SearchScreen
import com.clownteam.ui_search.ui.SearchViewModel
import com.example.ui_user_archive.ArchiveScreen
import com.example.ui_user_archive.ArchiveScreenViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.gson.Gson

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    imageLoader: ImageLoader,
    showBottomBar: (isShown: Boolean) -> Unit
) {
    val tokenManager = TokenManagerImpl.create()

    val startDestination = if (tokenManager.getToken() == null) {
        Route.LoginRoute.route
    } else {
        BottomNavItem.Home.route
    }

//    val startDestination = Route.AddToCollectionRoute.route

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { defaultEnterTransition() },
        exitTransition = { defaultExitTransition() },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() }
    ) {
        // *************************** Bottom Bar navigation ***************************

        bottomItemComposable(BottomNavItem.Home.route) {
            val viewModel: CourseListViewModel = hiltViewModel()
            CourseList(
                state = viewModel.state.value,
                eventHandler = viewModel,
                navigateToDetailScreen = { courseId ->
                    navController.navigate(Route.CourseRoute.getRouteWithArgument(courseId))
                },
                navigateToAddToCollection = { courseId ->
                    navController.navigate(Route.AddToCollectionRoute.getRouteWithArgument(courseId))
                },
                imageLoader = imageLoader,
                navigateToLogin = { defaultNavigateToLoginAction(navController) }
            )
            showBottomBar(true)
        }

        bottomItemComposable(BottomNavItem.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                state = viewModel.state,
                eventHandler = viewModel,
                imageLoader = imageLoader,
                coursesFlow = viewModel.coursesFlow,
                collectionsFlow = viewModel.collectionsFlow,
                navigateToLogin = { defaultNavigateToLoginAction(navController) },
                navigateToCourse = { courseId ->
                    navController.navigate(Route.CourseRoute.getRouteWithArgument(courseId))
                },
                navigateToCollection = { id ->
                    navController.navigate(Route.CourseCollectionRoute.getRouteWithArgument(id))
                }
            )
            showBottomBar(true)
        }

        bottomItemComposable(BottomNavItem.Archive.route) {
//            ArchiveContainer(
//                externalRouter = createExternalRouter { route, params, builder ->
//                    navController.navigate(route, params, builder)
//                },
//                imageLoader = imageLoader,
//                navigateToLogin = { defaultNavigateToLoginAction(navController) }
//            )

//            val viewModel: CollectionListViewModel = hiltViewModel()
//            CollectionList(
//                state = viewModel.state.value,
//                eventHandler = viewModel,
//                imageLoader = imageLoader,
//                navigateToDetailed = { id ->
//                    navController.navigate(Route.CourseCollectionRoute.getRouteWithArgument(id))
//                },
//                navigateToLogin = { defaultNavigateToLoginAction(navController) }
//            )
//            showBottomBar(true)

            val viewModel = hiltViewModel<ArchiveScreenViewModel>()
            ArchiveScreen(
                state = viewModel.state.value,
                eventHandler = viewModel,
                imageLoader = imageLoader,
                coursesFlow = viewModel.coursesFlow,
                collectionsFlow = viewModel.collectionsFlow,
                navigateToLogin = { defaultNavigateToLoginAction(navController) },
                navigateToCourse = { courseId ->
                    navController.navigate(Route.CourseRoute.getRouteWithArgument(courseId))
                },
                navigateToCollection = { id ->
                    navController.navigate(Route.CourseCollectionRoute.getRouteWithArgument(id))
                }
            )
            showBottomBar(true)
        }

        bottomItemComposable(BottomNavItem.Profile.route) {
            ProfileContainer(
                externalRouter = createExternalRouter { route, params, builder ->
                    navController.navigate(route, params, builder)
                },
                showBottomBar = showBottomBar,
                imageLoader = imageLoader,
                navigateToLogin = { defaultNavigateToLoginAction(navController) },
                navigateToCourse = { courseId ->
                    navController.navigate(Route.CourseRoute.getRouteWithArgument(courseId))
                },
                navigateToCollection = { id ->
                    navController.navigate(Route.CourseCollectionRoute.getRouteWithArgument(id))
                }
            )
            showBottomBar(true)
        }

        // *************************** Login navigation ***************************

        composable(Route.LoginRoute.route,
            enterTransition = {
                val offsetX = if (initialState.destination.route == Route.RegistrationRoute.route) {
                    { width: Int -> -width }
                } else {
                    { width: Int -> width }
                }
                slideInHorizontally(initialOffsetX = offsetX)
            }
        ) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                state = viewModel.state.value,
                eventHandler = viewModel,
                navigateToRegistration = { navController.navigate(Route.RegistrationRoute.route) },
                navigateToRestorePassword = { navController.navigate(Route.RestorePasswordRoute.route) },
                onSuccessLogin = { navController.navigate(BottomNavItem.Home.route) }
            )
            showBottomBar(false)
        }

        composable(Route.RegistrationRoute.route,
            exitTransition = {
                if ((initialState.destination.route == Route.LoginRoute.route)) {
                    slideOutHorizontally { -it }
                } else {
                    slideOutHorizontally { it }
                }
            }
        ) {
            val viewModel: RegistrationViewModel = hiltViewModel()
            RegistrationScreen(
                state = viewModel.state,
                eventHandler = viewModel,
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
            showBottomBar(false)
        }

        composable(Route.RestorePasswordRoute.route) {
            val viewModel: RestorePasswordViewModel = hiltViewModel()
            RestorePasswordScreen(
                state = viewModel.state,
                eventHandler = viewModel,
                navigateBack = { navController.navigateUp() }
            )
            showBottomBar(false)
        }

        // *************************** Collection navigation ***************************

        composable(Route.AddToCollectionRoute.route) {
            val viewModel: AddToCollectionScreenViewModel = hiltViewModel()
            AddToCollectionScreen(
                state = viewModel.state.value,
                eventHandler = viewModel,
                imageLoader = imageLoader,
                collectionsFlow = viewModel.collectionsFlow,
                onBack = { navController.navigateUp() },
                navigateToCreateCollection = { courseId ->
                    navController.navigate(Route.CreateCollectionRoute.getRouteWithArgument(courseId))
                },
                navigateToLogin = { defaultNavigateToLoginAction(navController) }
            )
            showBottomBar(false)
        }

        composable(Route.CreateCollectionRoute.route) {
            val viewModel: CreateCollectionViewModel = hiltViewModel()
            CreateCollectionScreen(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.navigateUp() },
                onSuccessCreate = {
                    navController.navigateUp()
                    navController.navigateUp()
                },
                navigateToLogin = { defaultNavigateToLoginAction(navController) }
            )
            showBottomBar(false)
        }

        composable(Route.CourseCollectionRoute.route) {
            val viewModel: CollectionDetailedViewModel = hiltViewModel()
            CollectionDetailed(
                state = viewModel.state.value,
                eventHandler = viewModel,
                imageLoader = imageLoader,
                onBackPressed = { navController.popBackStack() },
                navigateToLogin = { defaultNavigateToLoginAction(navController) },
                openCourse = { navController.navigate(Route.CourseRoute.getRouteWithArgument(it)) }
            )
            showBottomBar(false)
        }

        // *************************** Course navigation ***************************

        composable(
            route = Route.CourseRoute.route,
            arguments = Route.CourseRoute.arguments,
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            popEnterTransition = { slideInHorizontally { -1000 } }
        ) {
            CourseScreen(
                imageLoader,
                onBack = { navController.popBackStack() },
                navigateToPassing = { courseId ->
                    navController.navigate(Route.CourseModulesRoute.getRouteWithArgument(courseId))
                },
                navigateToLogin = { defaultNavigateToLoginAction(navController) },
                navigateToAddToCollection = { courseId ->
                    navController.navigate(Route.AddToCollectionRoute.getRouteWithArgument(courseId))
                }
            )
            showBottomBar(false)
        }

        composable(
            route = Route.CourseModulesRoute.route,
            arguments = Route.CourseModulesRoute.arguments
        ) {
            val viewModel: CourseModulesViewModel = hiltViewModel()
            CourseModules(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.navigateUp() },
                onModuleClick = { courseId, moduleId, moduleName ->
                    val args =
                        CourseLessonsViewModel.CourseLessonsArgs(courseId, moduleId, moduleName)
                    val argsJson = Uri.encode(Gson().toJson(args))
                    navController.navigate(Route.CourseLessonsRoute.getRouteWithArgument(argsJson))
                },
                navigateToLogin = { defaultNavigateToLoginAction(navController) }
            )
            showBottomBar(false)
        }

        composable(
            route = Route.CourseLessonsRoute.route,
            arguments = Route.CourseLessonsRoute.arguments
        ) {
            val viewModel: CourseLessonsViewModel = hiltViewModel()
            CourseLessons(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.navigateUp() },
                onLessonClick = { courseId, moduleId, lessonId, lessonName, stepId ->
                    val args = CourseStepsViewModel.CourseStepsArgs(
                        courseId, moduleId, lessonId, lessonName, stepId
                    )

                    val argsJson = Uri.encode(Gson().toJson(args))
                    navController.navigate(Route.CourseStepsRoute.getRouteWithArgument(argsJson))
                },
                navigateToLogin = { defaultNavigateToLoginAction(navController) }
            )
            showBottomBar(false)
        }

        composable(
            route = Route.CourseStepsRoute.route,
            arguments = Route.CourseStepsRoute.arguments
        ) {
            val viewModel: CourseStepsViewModel = hiltViewModel()
            CourseSteps(
                state = viewModel.state.value,
                eventHandler = viewModel,
                onBack = { navController.navigateUp() },
                navigateToLogin = { defaultNavigateToLoginAction(navController) }
            )
            showBottomBar(false)
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

private fun bottomItemEnterTransition(): EnterTransition = fadeIn(animationSpec = tween(700))

private fun bottomItemExitTransition(): ExitTransition = fadeOut(animationSpec = tween(700))

private fun defaultNavigateToLoginAction(navController: NavController) {
    navController.navigate(Route.LoginRoute.route) {
        popUpTo(BottomNavItem.Home.route) {
            inclusive = true
        }
    }
}
