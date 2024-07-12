package com.service.techit.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.runtime.collectAsState
import kotlinx.parcelize.Parcelize
import org.intellij.lang.annotations.Language

@Keep
@Parcelize
data class TechITStyles(var language: TechITLanguage?=TechITLanguage(),var backgroundColor: String?="#E0B6EA", var topAppBarColor: String?="#E0B6EA", var headerColor: String?="#000000", var contentColor: String?="#000000", var contentBGColor: String?="#ffffff",var landingImages:ArrayList<String>?= arrayListOf("https://d2wuvg8krwnvon.cloudfront.net/media/user_space/5f82b9566cb3/navigation_slider/USR_USR_navigation_slider_1694899415_7446.png"
    ,"https://d2wuvg8krwnvon.cloudfront.net/media/user_space/5f82b9566cb3/navigation_slider/USR_USR_navigation_slider_1694899461_8268.png"
    ,"https://d2wuvg8krwnvon.cloudfront.net/media/user_space/5f82b9566cb3/navigation_slider/USR_USR_navigation_slider_1694899504_5352.png"
    ,"https://d2wuvg8krwnvon.cloudfront.net/media/user_space/5f82b9566cb3/navigation_slider/USR_USR_navigation_slider_1695074876_8879.png")):Parcelable
