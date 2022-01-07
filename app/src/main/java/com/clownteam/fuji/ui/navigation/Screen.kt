package com.clownteam.fuji.ui.navigation

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    object MainTab: Screen(route = "main_tab_screen")

}