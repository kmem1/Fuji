package com.clownteam.components.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    val context = LocalContext.current
    Toast.makeText(context, text, duration).show()
}