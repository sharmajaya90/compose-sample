package com.service.composesample.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_userinfo")
class UserInfoEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "user_name")
    var name: String? = null
    @ColumnInfo(name = "user_email")
    var email: String? = null
    @ColumnInfo(name = "user_address")
    var address: String? = null
}
