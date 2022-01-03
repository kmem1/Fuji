package com.clownteam.fuji.ui.navigation.bottom_navigation

import androidx.annotation.DrawableRes
import com.clownteam.fuji.R

sealed class BottomNavItem(val route: String, @DrawableRes val iconId: Int, val title: String) {

    object Home : BottomNavItem("home_route", R.drawable.ic_outline_home_24, "Home")
    object Search : BottomNavItem("search_route", R.drawable.ic_baseline_search_24, "Search")
    object Profile : BottomNavItem("profile_route", R.drawable.ic_outline_account_circle_24, "Profile")

}