package com.clownteam.fuji.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clownteam.core.domain.EventHandler
import com.clownteam.course_domain.Course
import com.clownteam.fuji.ui.SplashScreen
import com.clownteam.fuji.ui.navigation.bottom_navigation.BottomNavItem
import com.clownteam.ui_courselist.ui.CourseList
import com.clownteam.ui_courselist.ui.CourseListEvent
import com.clownteam.ui_courselist.ui.CourseListState


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            CourseList(
                state = CourseListState(courses = testCourses),
                eventHandler = object : EventHandler<CourseListEvent> {
                    override fun obtainEvent(event: CourseListEvent) {}
                },
                navigateToDetailScreen = { _ -> }
            )
        }

        composable(BottomNavItem.Search.route) {
            Scaffold {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Search"
                    )
                }
            }
        }

        composable(BottomNavItem.Profile.route) {
            Scaffold {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Profile"
                    )
                }
            }
        }
    }
}

private val testCourses = listOf(
    Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 100054654F,
        durationInMinutes = 3676,
        rating = 4.6F,
        marksCount = 33456,
        membersAmount = 3965,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс fdsf eqwfasdqwdas d"
    ),
    Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 0F,
        durationInMinutes = 3676,
        rating = 4.6F,
        marksCount = 33456,
        membersAmount = 396565,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс"
    ),
    Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 1000F,
        durationInMinutes = 36764,
        rating = 4.6F,
        marksCount = 6574,
        membersAmount = 39657,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс"
    ),
    Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 100054654F,
        durationInMinutes = 3676,
        rating = 4.6F,
        marksCount = 33456,
        membersAmount = 3965,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс fdsf eqwfasdqwdas d"
    ),
    Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 0F,
        durationInMinutes = 3676,
        rating = 4.6F,
        marksCount = 33456,
        membersAmount = 396565,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс"
    ),
    Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 1000F,
        durationInMinutes = 36764,
        rating = 4.6F,
        marksCount = 6574,
        membersAmount = 39657,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс"
    )
)