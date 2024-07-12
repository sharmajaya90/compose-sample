package com.service.techit.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class DisplayableDateDMY(
    val timeISO: String?,
    val dateFormatFromServer: String = "yyyy-MM-dd'T'HH:mm:ss",
    var date: Date? = null

) : Parcelable {
    init {
        timeISO?.let {
            val isoDateFormat = SimpleDateFormat(dateFormatFromServer, Locale.ENGLISH)
            kotlin.runCatching {
                date = isoDateFormat.parse(it)
            }
        }
    }
}