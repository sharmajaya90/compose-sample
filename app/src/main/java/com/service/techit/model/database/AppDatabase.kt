package com.service.techit.model.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.model.database.dao.UserInfoDao
import com.service.techit.model.database.entity.QAInfoEntity
import com.service.techit.model.database.entity.UserInfoEntity
/*
autoMigrations = [AutoMigration(from = 1, to = 2)] to migrate database if any db schema getting changed
AutoMigration(from = 1, to = 2, spec = AppDatabase.Migration1To2::class)
@RenameColumn(tableName = "table_userinfo", fromColumnName = "user_address", toColumnName = "user_detailed")
    class Migration1To2:AutoMigrationSpec
    for migration spec
 */

@Database(entities = [UserInfoEntity::class,QAInfoEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase :RoomDatabase(){
    abstract fun provideUserInfoDao(): UserInfoDao
    abstract fun provideQAInfoDao(): QAInfoDao
}