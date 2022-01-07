package com.clownteam.course_datasource.cache

import com.clownteam.course_domain.Course
import kotlinx.coroutines.delay
import kotlin.random.Random

class CourseCacheImpl : CourseCache {

    override suspend fun getPopularCourses(): List<Course> {
        delay(Random.nextLong(500, 2000))

        return listOf(
            Course(
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
            ), Course(
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

    override suspend fun getMyCourses(): List<Course> {
        delay(Random.nextLong(500, 2000))

        return listOf(
            Course(
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