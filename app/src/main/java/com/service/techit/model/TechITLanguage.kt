package com.service.techit.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class TechITLanguage( var qaTitle: String?="Topic", var qaDetailed: String?="Explanation", var noData: String?="Will available soon..."):Parcelable
