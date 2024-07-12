package com.service.composesample.di.component

import android.content.Context
import com.service.composesample.model.database.AppDatabase
import com.service.composesample.model.database.dao.UserInfoDao
import com.service.composesample.di.module.ContextModule
import com.service.composesample.di.module.DataModule
import com.service.composesample.di.module.NetworkModule
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
}