package com.clownteam.fuji.ui.navigation.nav_types

import android.os.Bundle
import androidx.navigation.NavType
import com.clownteam.ui_coursepassing.course_steps.CourseStepsViewModel
import com.google.gson.Gson

class CourseStepsNavType :
    NavType<CourseStepsViewModel.CourseStepsArgs>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): CourseStepsViewModel.CourseStepsArgs? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): CourseStepsViewModel.CourseStepsArgs {
        return Gson().fromJson(value, CourseStepsViewModel.CourseStepsArgs::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: CourseStepsViewModel.CourseStepsArgs) {
        bundle.putParcelable(key, value)
    }
}