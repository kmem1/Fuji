package com.clownteam.fuji.ui.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.fuji.ui.navigation.screens.archive.ArchiveContainer
import com.clownteam.fuji.ui.navigation.screens.course.CourseScreen
import com.clownteam.fuji.ui.navigation.screens.profile.ProfileContainer
import com.clownteam.fuji.ui.navigation.screens.search.SearchScreen
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
import com.google.gson.Gson

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
                imageLoader = imageLoader
            )
        }

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

        composable(
            route = Route.CourseRoute.route,
            arguments = Route.CourseRoute.arguments
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

        composable(BottomNavItem.Search.route) {
            SearchScreen()
        }

        composable(BottomNavItem.Archive.route) {
            showBottomBar(true)
            ArchiveContainer(
                externalRouter = createExternalRouter { route, params ->
                    navController.navigate(route, params)
                },
                imageLoader = imageLoader
            )
        }

        composable(BottomNavItem.Profile.route) {
            ProfileContainer(createExternalRouter { route, params ->
                navController.navigate(route, params)
            })
        }
    }
}