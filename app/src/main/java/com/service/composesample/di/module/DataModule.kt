package com.service.composesample.di.module

import android.content.Context
import androidx.room.Room
import com.service.composesample.model.database.AppDatabase
import com.service.composesample.model.database.dao.UserInfoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ContextModule::class])
class DataModule {
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "techit_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }


    @Provides
    @Singleton
    fun provideUserInfoDao(database: AppDatabase): UserInfoDao {
        return database.provideUserInfoDao()
    }

}