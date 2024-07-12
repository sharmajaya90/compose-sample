package com.service.techit.di.component

import android.content.Context
import com.service.techit.model.database.AppDatabase
import com.service.techit.model.database.dao.UserInfoDao
import com.service.techit.di.module.ContextModule
import com.service.techit.di.module.DataModule
import com.service.techit.di.module.NetworkModule
import com.service.techit.model.database.dao.QAInfoDao
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,NetworkModule::class,DataModule::class])
interface CoreComponent {
    fun context(): Context
    fun retrofit(): Retrofit
    fun provideAppDatabase(): AppDatabase
    fun provideUserInfoDao(): UserInfoDao
    fun provideQAInfoDao(): QAInfoDao
}