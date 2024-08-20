package com.euriconeto.markupcalculator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = lightGreen,
    onPrimary = Color.White,
    primaryContainer = darkGreen,
    onPrimaryContainer = Color.Black,
    secondary = cleanGreen,
    onSecondary = Color.White,
    secondaryContainer = swampGreen,
    onSecondaryContainer = Color.Black,
    tertiary = babyBlue,
    onTertiary = Color.White,
    tertiaryContainer = oilBlue,
    onTertiaryContainer = Color.Black,
    error = red,
    errorContainer = lightRed,
    background = grey
)

private val LightColorScheme = lightColorScheme(
    primary = darkGreen,
    onPrimary = Color.White,
    primaryContainer = lightGreen,
    onPrimaryContainer = Color.Black,
    secondary = swampGreen,
    onSecondary = Color.White,
    secondaryContainer = cleanGreen,
    onSecondaryContainer = Color.Black,
    tertiary = oilBlue,
    onTertiary = Color.White,
    tertiaryContainer = babyBlue,
    onTertiaryContainer = Color.Black,
    error = red,
    errorContainer = lightRed,
    background = grey
)

@Composable
fun MarkupCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}