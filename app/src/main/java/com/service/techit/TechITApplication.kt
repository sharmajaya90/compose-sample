package com.service.techit

import android.app.Application
import com.service.techit.di.component.CoreComponent
import com.service.techit.di.component.CoreComponentProvider
import com.service.techit.di.component.DaggerCoreComponent
import com.service.techit.di.module.ContextModule

class TechITApplication:Application(),CoreComponentProvider {
    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        coreComponent = DaggerCoreComponent.builder().contextModule(ContextModule(this)).build()
    }

    override fun provideCoreComponent(): CoreComponent = coreComponent
}