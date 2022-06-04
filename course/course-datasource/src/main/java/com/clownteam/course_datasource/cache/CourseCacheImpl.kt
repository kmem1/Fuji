package com.clownteam.course_datasource.cache

import com.clownteam.core.domain.SResult
import com.clownteam.core.utils.extensions.emptyFailed
import com.clownteam.core.utils.extensions.toSuccessResult
import com.clownteam.course_domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

internal class CourseCacheImpl : CourseCache {

    override suspend fun getPopularCourses(): SResult<List<Course>> = withContext(Dispatchers.IO) {
        delay(Random.nextLong(500, 2000))

        courses.toSuccessResult()
    }

    override suspend fun getMyCourses(): SResult<List<Course>> = withContext(Dispatchers.IO) {
        delay(Random.nextLong(500, 2000))

        courses.subList(0, 3).toSuccessResult()
    }

    override suspend fun getCourse(id: String): SResult<Course> = withContext(Dispatchers.IO) {
        delay(Random.nextLong(500, 2000))

        courses.firstOrNull { it.id == id }?.toSuccessResult() ?: emptyFailed()
    }

    override suspend fun getCourseInfo(courseId: String): SResult<CourseInfo> =
        withContext(Dispatchers.IO) {
            delay(Random.nextLong(500, 2000))

            courseInfoList.firstOrNull { it.courseId == courseId }?.toSuccessResult()
                ?: emptyFailed()
        }

    private val courses = listOf(
        Course(
            id = "1",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            imgUrl = "https://i.ibb.co/LdhM7Bp/10.png",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс",
            courseDurationInHours = 89
        ),
        Course(
            id = "2",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            imgUrl = "https://i.ibb.co/9hhTj2Q/12.jpg",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс",
            courseDurationInHours = 89
        ),
        Course(
            id = "3",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            imgUrl = "https://i.ibb.co/9hhTj2Q/12.jpg",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс",
            courseDurationInHours = 89
        ), Course(
            id = "4",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            imgUrl = "https://i.ibb.co/LdhM7Bp/10.png",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс",
            courseDurationInHours = 89
        ),
        Course(
            id = "5",
            title = "Основы программирования на C++",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            imgUrl = "https://i.ibb.co/LdhM7Bp/10.png",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс",
            courseDurationInHours = 89
        ),
        Course(
            id = "6",
            title = "Android Development",
            description = "Здесь последовательно излагаются понятия языка и постепенно усваиваются типовые алгоритмы.",
            imgUrl = "https://i.ibb.co/9hhTj2Q/12.jpg",
            price = 1000F,
            durationInMinutes = 3676,
            rating = 4.6F,
            marksCount = 33456,
            membersAmount = 3965,
            hasCertificate = true,
            maxProgressPoints = 65,
            currentPoints = 23,
            authorName = "Академия Яндекс",
            courseDurationInHours = 89
        )
    )

    private val forWhomItems = listOf(
        ForWhomCourseDescriptionItem(
            title = "Новичкам в коде",
            description = "Тем, кто только решил стать программистом, но не выбрал, в какой сфере собирается работать."
        ),
        ForWhomCourseDescriptionItem(
            title = "Начинающим разработчикам",
            description = "Тем, кто только решил стать программистом, но не выбрал, в какой сфере собирается работать."
        )
    )

    private val goalDescription =
        "Помочь тебе сделать первый шаг к профессии Программиста C++. За 93 часа ты научишься создавать простые программы для облегчения своей жизни. А также здесь должен быть какой-то текст, но его нет. Мир жесток и не справедлив. Но увы, нужно заполнять пустые места"

    private val learningSkills = listOf(
        "Использовать промышленные средства разработки: Git, CLion",
        "Умение работать с Объектно Ориентированным Программированием (ООП)"
    )

    private val modules = listOf(
        ModuleItem(
            title = "Базовый синтаксис",
            steps = listOf(
                "Типы данных",
                "Ввод вывод",
                "Простая арифметика",
                "Логические выражения",
                "Цикл While"
            )
        ),
        ModuleItem(
            title = "Базовый синтаксис jfgdljsfldjslkgfdjlkjasflkj lgfdsklgfd",
            steps = listOf(
                "Типы данных",
                "Ввод вывод",
                "Простая арифметика",
                "Логические выражения",
                "Цикл While"
            )
        ),
        ModuleItem(
            title = "Базовый синтаксис",
            steps = listOf(
                "Типы данных",
                "Ввод вывод",
                "Простая арифметика",
                "Логические выражения",
                "Цикл While"
            )
        )
    )

    private val percentageMap = mapOf(
        5 to 69,
        4 to 25,
        3 to 4,
        2 to 1,
        1 to 0
    )

    private val reviewItems = listOf(
        ReviewItem(
            avatarUrl = "https://i.ibb.co/Bf4KMdp/logo-person.jpg",
            userName = "Александр Дмитриевич",
            content = "Lorem Ipsum является текст-заполнитель обычно используется в графических, печать и издательской индустрии для предварительного просмотра макета и визуальных макетах",
            courseRating = 4,
            dateString = "2 декабря 2021"
        ),

        ReviewItem(
            avatarUrl = "https://i.ibb.co/Bf4KMdp/logo-person.jpg",
            userName = "Дементий Марк Иваныч",
            content = "Прекрасное творение Хаоса предпало моему взору",
            courseRating = 5,
            dateString = "2 декабря 2021"
        ),

        ReviewItem(
            avatarUrl = "https://i.ibb.co/Bf4KMdp/logo-person.jpg",
            userName = "Очередной чел",
            content = "Nice. Just nice.",
            courseRating = 5,
            dateString = "2 декабря 2021"
        )
    )

    private val courseInfoList = listOf(
        CourseInfo(
            courseId = "1",
            goalDescription = goalDescription,
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = learningSkills,
            moduleItems = modules,
            starsPercentage = percentageMap,
            reviewItems = reviewItems
        ),
        CourseInfo(
            courseId = "2",
            goalDescription = goalDescription,
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = learningSkills,
            moduleItems = modules,
            starsPercentage = percentageMap,
            reviewItems = reviewItems
        ),
        CourseInfo(
            courseId = "3",
            goalDescription = goalDescription,
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = learningSkills,
            moduleItems = modules,
            starsPercentage = percentageMap,
            reviewItems = reviewItems
        ),
        CourseInfo(
            courseId = "4",
            goalDescription = goalDescription,
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = learningSkills,
            moduleItems = modules,
            starsPercentage = percentageMap,
            reviewItems = reviewItems
        ),
        CourseInfo(
            courseId = "5",
            goalDescription = goalDescription,
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = learningSkills,
            moduleItems = modules,
            starsPercentage = percentageMap,
            reviewItems = reviewItems
        ),
        CourseInfo(
            courseId = "6",
            goalDescription = goalDescription,
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = learningSkills,
            moduleItems = modules,
            starsPercentage = percentageMap,
            reviewItems = reviewItems
        )
    )
}