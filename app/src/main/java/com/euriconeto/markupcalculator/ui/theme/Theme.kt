package com.euriconeto.markupcalculator.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = lightBlue,
    onPrimary = closeBlue,
    secondary = oilBlue,
    onSecondary = darkOilBlue,
    tertiary = lightPurple,
    onTertiary = darkPurple,
    primaryContainer = darkOilBlueInverse,
    onPrimaryContainer = oilBlueInverse,
    secondaryContainer = darkGreyBlue,
    onSecondaryContainer = lightGreyBlue,
    tertiaryContainer = purple,
    onTertiaryContainer = whitePurple,
    error = cleanRed,
    onError = blackRed,
    errorContainer = bloodRed,
    onErrorContainer = lightRed,
    surfaceDim = blackGray,
    surface = blackGray,
    surfaceBright = greyNormal,
    surfaceContainerLowest = dark,
    surfaceContainerLow = darkGrey90,
    surfaceContainer = darkGrey70,
    surfaceContainerHigh = darkGrey60,
    surfaceContainerHighest = darkGrey50,
    onSurface = grey40,
    onSurfaceVariant = grey05,
    outline = grey25,
    outlineVariant = grey75,
    inverseSurface = grey40,
    inverseOnSurface = grey80,
    inversePrimary = darkCyan
)

private val LightColorScheme = lightColorScheme(
    primary = darkCyan,
    onPrimary = white,
    secondary = rustCopper,
    onSecondary = white,
    tertiary = purpleTertiary,
    onTertiary = white,
    primaryContainer = blueBaby,
    onPrimaryContainer = deepBlue,
    secondaryContainer = cleanRustBlue,
    onSecondaryContainer = deepRust,
    tertiaryContainer = lavender,
    onTertiaryContainer = deepPurple,
    error = red,
    onError = white,
    errorContainer = lightRed,
    onErrorContainer = darkRed,
    surfaceDim = grey,
    surface = lightGrey,
    surfaceBright = lightGrey,
    surfaceContainerLowest = white,
    surfaceContainerLow = grey10,
    surfaceContainer = grey20,
    surfaceContainerHigh = grey30,
    surfaceContainerHighest = grey40,
    onSurface = darkGray,
    onSurfaceVariant = grey70,
    outline = grey60,
    outlineVariant = grey50,
    inverseSurface = grey80,
    inverseOnSurface = grey55,
    inversePrimary = lightBabyBlue
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