package com.example.ui_user_archive

import com.clownteam.components.UiText

data class ArchiveScreenState(
    val isLoading: Boolean = false,
    val query: String = "",
    val shouldSearchItems: Boolean = false,
    val errorMessage: UiText? = null,
    val isUnauthorized: Boolean = false
)
