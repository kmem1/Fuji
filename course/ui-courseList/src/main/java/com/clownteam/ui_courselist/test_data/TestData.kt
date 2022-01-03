package com.clownteam.ui_courselist.test_data

import com.clownteam.course_domain.Course

internal object TestData {
    val testCourse = Course(
        id = "583hgfhfgdsdfjtgj",
        title = "Основы программирования на C++",
        description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
        price = 1000F,
        durationInMinutes = 3676,
        rating = 4.6F,
        marksCount = 56456,
        membersAmount = 65423,
        hasCertificate = true,
        maxProgressPoints = 65,
        currentPoints = 23,
        authorName = "Академия Яндекс"
    )

    val testCourses = listOf(
        Course(
            id = "583hgfhfgdsdfjtgj",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс"
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
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс"
        )
    )
}