package com.service.techit.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.service.techit.extensions.colorStringToComposeColor
import com.service.techit.model.TechITStyles
import com.service.techit.utils.firebase.FirestoreHelper
import com.service.techit.view.theme.*



@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    var techITStyles: TechITStyles? by remember { mutableStateOf(TechITStyles()) }

    LaunchedEffect(Unit) {
        techITStyles  =   FirestoreHelper.provideTechITStyles()
    }

    val colors = if (darkTheme) {
        lightColors(
            primary = PrimaryColor,
            primaryVariant = PrimaryDarkColor,
            secondary = SecondaryColor,
            background = techITStyles?.backgroundColor?.colorStringToComposeColor()?:Color.White,
            surface = techITStyles?.backgroundColor?.colorStringToComposeColor()?:Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black,
        )
    } else {
        lightColors(
            primary = PrimaryColor,
            primaryVariant = PrimaryDarkColor,
            secondary = SecondaryColor,
            background = techITStyles?.backgroundColor?.colorStringToComposeColor()?:Color.White,
            surface = techITStyles?.backgroundColor?.colorStringToComposeColor()?:Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black,
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}