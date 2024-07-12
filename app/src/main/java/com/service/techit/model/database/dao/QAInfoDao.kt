package com.service.techit.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.service.techit.model.database.entity.QAInfoEntity
import com.service.techit.model.database.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface QAInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQAInfo(entity: QAInfoEntity): Long
    @Update
    fun updateQAInfo(entity: QAInfoEntity): Int
    @Delete
    fun delete(entity: QAInfoEntity): Int

    @Query("SELECT * FROM table_qainfo")
    fun fetchAllQA(): Flow<List<QAInfoEntity>>


    @Query("SELECT * FROM table_qainfo WHERE qa_type = :type")
    fun getQAByType(type: String): Flow<List<QAInfoEntity>>
}