package com.service.composesample.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.ui.graphics.Color


fun <T> String?.convertIntoModel(classRef: Class<T>): T? {
    return try {
        convertIntoModel(classRef = classRef, gson = this!!.provideGsonWithCoreJsonString())
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}


fun <T> String?.convertIntoModel(classRef: Class<T>, gson: Gson): T? {
    return try {
        gson.fromJson(this, classRef)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}

fun <T> String?.convertIntoModels(type: TypeToken<T>): T? {
    return try {
        this?.provideGsonWithCoreJsonString()?.fromJson(this, type.type)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
        null
    }
}

fun String.truncateText(limit: Int): String {
    return if (length > limit) {
        take(limit) + "..."
    } else {
        this
    }
}
fun String.colorStringToComposeColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

