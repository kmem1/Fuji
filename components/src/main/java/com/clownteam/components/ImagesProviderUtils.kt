package com.clownteam.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object ImagesProviderUtils {

    fun getImageFileByUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val imageFile = File(context.externalCacheDir, "temp.jpg")

        val outputStream = FileOutputStream(imageFile)

        inputStream.use { stream ->
            val buffer = ByteArray(4 * 1024)
            var read = 0

            while (true) {
                read = stream?.read(buffer) ?: -1
                if (read == -1) break
                outputStream.write(buffer, 0, read)
            }

            outputStream.flush()
        }

        return imageFile
    }

    fun getImageBitmapByUri(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }
}