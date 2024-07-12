package com.service.composesample.view.di

import com.service.composesample.di.component.CoreComponent
import com.service.composesample.view.HomeActivity
import dagger.Component

@CommonViewScope
@Component(dependencies = [CoreComponent::class])
interface CommonUIComponent {
   fun inject(mainActivity: HomeActivity)
}