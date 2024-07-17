package com.service.composesample.model

import com.service.composesample.model.database.entity.UserInfoEntity

fun UserInfo.toUser() =
    UserInfo(
        name = name,
        email = email,
        address = address
    )
fun UserInfoEntity.toUser() =
    UserInfo(
        name = name?:"" ,
        email = email?:"" ,
        address = address?:""
    )