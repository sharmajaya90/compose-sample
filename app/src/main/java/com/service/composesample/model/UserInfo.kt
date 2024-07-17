package com.service.composesample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(var key:String?="",var name:String,var email:String,var address:String="",var image:String?=null):Parcelable
