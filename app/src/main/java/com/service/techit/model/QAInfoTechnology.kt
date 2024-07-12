package com.service.techit.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class QAInfoTechnology(var id: Int =0, var title: String?="", var name: String?=""):Parcelable