package com.service.techit.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_qainfo")
class QAInfoEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "qa_title")
    var title: String? = null
    @ColumnInfo(name = "qa_type")
    var type: String? = null
    @ColumnInfo(name = "qa_detailed")
    var detailed: String? = null
}
