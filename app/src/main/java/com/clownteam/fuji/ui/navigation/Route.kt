package com.clownteam.fuji.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.clownteam.ui_coursedetailed.ui.CourseDetailedViewModel

sealed class Route(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    object HomeRoute : Route(
        route = "home_route"
    )

    object CourseRoute: Route(
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
}