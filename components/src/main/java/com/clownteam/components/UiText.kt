package com.clownteam.components

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

sealed class UiText {

    data class DynamicString(val value: String): UiText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiText()

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }

    fun showToast(context: Context) {
        Toast.makeText(context, this.asString(context), Toast.LENGTH_SHORT).show()
    }
}