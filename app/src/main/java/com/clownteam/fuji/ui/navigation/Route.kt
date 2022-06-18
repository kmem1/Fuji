package com.clownteam.fuji.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.clownteam.ui_collectiondetailed.ui.CollectionDetailedViewModel
import com.clownteam.ui_coursedetailed.ui.CourseDetailedViewModel
import com.clownteam.ui_coursepassing.course_modules.CourseModulesViewModel

sealed class Route(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    object HomeRoute : Route(
        route = "home_route"
    )

    object CourseRoute : Route(
        route = "course_route",
        arguments = listOf(navArgument(CourseDetailedViewModel.COURSE_ID_ARG_KEY) {
            type = NavType.StringType
        })
    )

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
}