package com.service.composesample

import android.app.Application
import com.service.composesample.di.component.CoreComponent
import com.service.composesample.di.component.CoreComponentProvider
import com.service.composesample.di.component.DaggerCoreComponent
import com.service.composesample.di.module.ContextModule

class ComposeApplication:Application(),CoreComponentProvider {
    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        coreComponent = DaggerCoreComponent.builder().contextModule(ContextModule(this)).build()
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent
}