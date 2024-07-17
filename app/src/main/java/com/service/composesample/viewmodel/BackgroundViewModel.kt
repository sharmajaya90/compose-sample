package com.service.composesample.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream


class BackgroundViewModel : ViewModel() {

    suspend fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


}