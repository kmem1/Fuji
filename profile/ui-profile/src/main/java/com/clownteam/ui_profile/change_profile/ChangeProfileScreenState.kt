package com.clownteam.ui_profile.change_profile

import android.graphics.Bitmap
import com.clownteam.components.UiText
import java.io.File

data class ChangeProfileScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val avatarUrl: String? = null,
    val username: String = "",
    val usernameError: UiText? = null,
    val message: UiText? = null,
    val isUnauthorized: Boolean = false,
    val imageFile: File? = null,
    val imageFileBitmap: Bitmap? = null
)