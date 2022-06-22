package com.clownteam.fuji.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.clownteam.fuji.ui.navigation.nav_types.CourseLessonNavType
import com.clownteam.fuji.ui.navigation.nav_types.CourseStepsNavType
import com.clownteam.ui_collectionaction.add_to_collection.AddToCollectionScreenViewModel
import com.clownteam.ui_collectionaction.create_collection.CreateCollectionViewModel
import com.clownteam.ui_collectiondetailed.ui.CollectionDetailedViewModel
import com.clownteam.ui_coursedetailed.ui.CourseDetailedViewModel
import com.clownteam.ui_coursepassing.course_lessons.CourseLessonsViewModel
import com.clownteam.ui_coursepassing.course_modules.CourseModulesViewModel
import com.clownteam.ui_coursepassing.course_steps.CourseStepsViewModel

sealed class Route(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    object HomeRoute : Route(
        route = "home_route"
    )

    object CourseRoute : Route(
        route = "course_route/{${CourseDetailedViewModel.COURSE_ID_ARG_KEY}}",
        arguments = listOf(navArgument(CourseDetailedViewModel.COURSE_ID_ARG_KEY) {
            type = NavType.StringType
        })
    ) {
        fun getRouteWithArgument(args: String): String {
            return CourseRoute.route.replace(
                "{${CourseDetailedViewModel.COURSE_ID_ARG_KEY}}",
                args
            )
        }
    }

    object SearchRoute : Route(route = "search_route")

    object ProfileRoute : Route(route = "profile_route")

    object LoginRoute : Route(route = "login_route")

    object RegistrationRoute : Route(route = "registration_route")

    object RestorePasswordRoute : Route(route = "restore_password_route")

    object ArchiveRoute : Route(route = "archive_route")

    object CourseCollectionRoute : Route(
        route = "course_collection/{${CollectionDetailedViewModel.COLLECTION_ID_ARG_KEY}}",
        arguments = listOf(
            navArgument(CollectionDetailedViewModel.COLLECTION_ID_ARG_KEY) {
                type = NavType.StringType
            })
    ) {
        fun getRouteWithArgument(collectionId: String): String {
            return route.replace(
                "{${CollectionDetailedViewModel.COLLECTION_ID_ARG_KEY}}",
                collectionId
            )
        }
    }

    object AddToCollectionRoute : Route(
        route = "add_to_collection/{${AddToCollectionScreenViewModel.COURSE_ID_ARG_KEY}}",
        arguments = listOf(
            navArgument(AddToCollectionScreenViewModel.COURSE_ID_ARG_KEY) {
                type = NavType.StringType
            })
    ) {
        fun getRouteWithArgument(args: String): String {
            return route.replace(
                "{${AddToCollectionScreenViewModel.COURSE_ID_ARG_KEY}}",
                args
            )
        }
    }

    object CreateCollectionRoute : Route(
        route = "create_collection_with_course/{${CreateCollectionViewModel.COURSE_ID_ARG_KEY}}",
        arguments = listOf(
            navArgument(CreateCollectionViewModel.COURSE_ID_ARG_KEY) {
                type = NavType.StringType
            })
    ) {
        fun getRouteWithArgument(args: String): String {
            return route.replace(
                "{${CreateCollectionViewModel.COURSE_ID_ARG_KEY}}",
                args
            )
        }
    }

    object CourseModulesRoute : Route(
        route = "course_modules/{${CourseModulesViewModel.COURSE_ID_ARG_KEY}}",
        arguments = listOf(
            navArgument(CourseModulesViewModel.COURSE_ID_ARG_KEY) {
                type = NavType.StringType
            })
    ) {
        fun getRouteWithArgument(courseId: String): String {
            return route.replace(
                "{${CourseModulesViewModel.COURSE_ID_ARG_KEY}}",
                courseId
            )
        }
    }

    object CourseLessonsRoute : Route(
        route = "course_lessons/{${CourseLessonsViewModel.LESSONS_ARG_KEY}}",
        arguments = listOf(
            navArgument(CourseLessonsViewModel.LESSONS_ARG_KEY) {
                type = CourseLessonNavType()
            })
    ) {
        fun getRouteWithArgument(args: String): String {
            return route.replace(
                "{${CourseLessonsViewModel.LESSONS_ARG_KEY}}",
                args
            )
        }
    }

    object CourseStepsRoute : Route(
        route = "course_steps/{${CourseStepsViewModel.STEPS_ARG_KEY}}",
        arguments = listOf(
            navArgument(CourseStepsViewModel.STEPS_ARG_KEY) {
                type = CourseStepsNavType()
            })
    ) {
        fun getRouteWithArgument(args: String): String {
            return route.replace(
                "{${CourseStepsViewModel.STEPS_ARG_KEY}}",
                args
            )
        }
    }
}