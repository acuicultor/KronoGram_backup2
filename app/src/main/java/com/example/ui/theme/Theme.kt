package com.example.ui.theme

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

val AmoledDarkColorScheme = darkColorScheme(
    primary = ChronoCyan,
    onPrimary = AmoledBlack,
    primaryContainer = ChronoBlueDeep,
    onPrimaryContainer = ChronoCyanBright,
    secondary = ChronoBlueNeon,
    onSecondary = Color.White,
    secondaryContainer = AmoledSurfaceVariant,
    onSecondaryContainer = ChronoTextWhite,
    tertiary = ChronoGold,
    background = AmoledBlack,
    onBackground = ChronoTextWhite,
    surface = AmoledSurface,
    onSurface = ChronoTextWhite,
    surfaceVariant = AmoledSurfaceVariant,
    onSurfaceVariant = ChronoTextDim,
    outline = AmoledBorder,
    error = ChronoDestructRed,
    onError = Color.White
)

val NightColorScheme = darkColorScheme(
    primary = ChronoCyan,
    onPrimary = NightBackground,
    primaryContainer = NightSurfaceVariant,
    onPrimaryContainer = ChronoCyanBright,
    secondary = ChronoBlueNeon,
    onSecondary = Color.White,
    secondaryContainer = NightCard,
    onSecondaryContainer = ChronoTextWhite,
    tertiary = ChronoGold,
    background = NightBackground,
    onBackground = ChronoTextWhite,
    surface = NightSurface,
    onSurface = ChronoTextWhite,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = ChronoTextDim,
    outline = NightCard,
    error = ChronoDestructRed,
    onError = Color.White
)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightSecondary,
    onPrimaryContainer = Color.White,
    secondary = LightSecondary,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = ChronoDestructRed,
    onError = Color.White
)

enum class KronoThemeMode {
    AMOLED_PURE,
    DARK_NIGHT,
    LIGHT_DAY,
    SYSTEM
}

@Composable
fun KronoGramTheme(
    themeMode: KronoThemeMode = KronoThemeMode.AMOLED_PURE,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val colorScheme = when (themeMode) {
        KronoThemeMode.AMOLED_PURE -> AmoledDarkColorScheme
        KronoThemeMode.DARK_NIGHT -> NightColorScheme
        KronoThemeMode.LIGHT_DAY -> LightColorScheme
        KronoThemeMode.SYSTEM -> if (systemDark) AmoledDarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
