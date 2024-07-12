package com.service.techit.extensions

import android.app.Activity
import com.service.techit.di.component.CoreComponentProvider


fun Activity.coreComponent() =
    (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
        ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")