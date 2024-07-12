package com.service.composesample.extensions

import android.app.Activity
import com.service.composesample.di.component.CoreComponentProvider


fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")