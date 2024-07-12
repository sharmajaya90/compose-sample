package com.service.techit.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class QAInfo(var id: String?="",var key: String?="",var title: String?="",var type: String?="",var detailed: String?=""):Parcelable