package com.clownteam.course_datasource.cache

import com.clownteam.course_domain.Course

class CourseCacheImpl : CourseCache {

    override fun getCurrentCourse(): Course {
        return Course(
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
        )
    }

    override fun getMyCourses(): List<Course> {
        return listOf(
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
                price = 1000F,
                durationInMinutes = 3676,
                rating = 4.6F,
                marksCount = 33456,
                membersAmount = 3965,
                hasCertificate = true,
                maxProgressPoints = 65,
                currentPoints = 23,
                authorName = "Академия Яндекс"
            )
        )
    }
}