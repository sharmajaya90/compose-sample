package com.service.techit.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val application: Application) {
    @Singleton
    @Provides
    fun context(): Context = application
}
