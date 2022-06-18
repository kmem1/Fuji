package com.clownteam.fuji.ui.navigation.nav_types

import android.os.Bundle
import androidx.navigation.NavType
import com.clownteam.ui_coursepassing.course_lessons.CourseLessonsViewModel
import com.google.gson.Gson

class CourseLessonNavType :
    NavType<CourseLessonsViewModel.CourseLessonsArgs>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): CourseLessonsViewModel.CourseLessonsArgs? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): CourseLessonsViewModel.CourseLessonsArgs {
        return Gson().fromJson(value, CourseLessonsViewModel.CourseLessonsArgs::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: CourseLessonsViewModel.CourseLessonsArgs) {
        bundle.putParcelable(key, value)
    }
}