package com.service.composesample.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.service.composesample.model.database.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(entity: UserInfoEntity): Long
    @Update
    fun updateUserInfo(entity: UserInfoEntity): Int
    @Delete
    fun delete(entity: UserInfoEntity): Int
    @Query("SELECT * FROM table_userinfo")
    fun fetchAllRegisterUsers(): Flow<List<UserInfoEntity>>

    @Query("DELETE FROM table_userinfo WHERE id = :id")
    fun deleteUserInfoWithId(id: Long): Int

    @Query("SELECT * FROM table_userinfo WHERE user_email = :email LIMIT 1")
    fun getUserByEmail(email: String): UserInfoEntity?
}