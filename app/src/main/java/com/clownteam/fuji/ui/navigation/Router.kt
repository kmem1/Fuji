package com.clownteam.fuji.ui.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavOptionsBuilder

fun createExternalRouter(block: (String, Bundle, NavOptionsBuilder.() -> Unit) -> Unit): Router =
    object : Router {
        override fun routeTo(
            screen: String,
            params: Bundle,
            builder: NavOptionsBuilder.() -> Unit
        ) {
            block.invoke(screen, params, builder)
        }
    }

interface Router {
    fun routeTo(
        screen: String,
        params: Bundle = bundleOf(),
        builder: NavOptionsBuilder.() -> Unit = {}
    ) {
        throw NotImplementedError(message = "You used router, but don't implemented it for screen $screen with params $params")
    }
}