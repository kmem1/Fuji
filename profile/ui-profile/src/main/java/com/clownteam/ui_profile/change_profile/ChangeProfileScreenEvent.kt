package com.clownteam.ui_profile.change_profile

import android.graphics.Bitmap
import com.clownteam.ui_profile.profile.ProfileEvent
import java.io.File

sealed class ChangeProfileScreenEvent {

    class SetImage(val file: File, val bitmap: Bitmap) : ChangeProfileScreenEvent()
    class SetUsername(val username: String) : ChangeProfileScreenEvent()
    object GetProfileData : ChangeProfileScreenEvent()
    object ApplyChanges : ChangeProfileScreenEvent()
    object MessageShown : ChangeProfileScreenEvent()
}
