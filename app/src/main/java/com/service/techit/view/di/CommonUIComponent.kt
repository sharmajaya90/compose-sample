package com.service.techit.view.di

import com.service.techit.di.component.CoreComponent
import com.service.techit.view.HomeActivity
import dagger.Component

@CommonViewScope
@Component(dependencies = [CoreComponent::class])
interface CommonUIComponent {
   fun inject(mainActivity: HomeActivity)
}