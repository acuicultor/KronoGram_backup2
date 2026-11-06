package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
    primary = TelegramDayPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF648DAF),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF2481CC),
    onSecondary = Color.White,
    secondaryContainer = TelegramDaySurfaceVariant,
    onSecondaryContainer = TelegramDayTextPrimary,
    background = TelegramDayBackground,
    onBackground = TelegramDayTextPrimary,
    surface = TelegramDaySurface,
    onSurface = TelegramDayTextPrimary,
    surfaceVariant = TelegramDaySurfaceVariant,
    onSurfaceVariant = TelegramDayTextSecondary,
    outline = Color(0xFFD6DCE2),
    error = ChronoDestructRed,
    onError = Color.White
)

enum class KronoThemeMode {
    AMOLED_PURE,
    DARK_NIGHT,
    LIGHT_DAY
}

@Composable
fun KronoGramTheme(
    themeMode: KronoThemeMode = KronoThemeMode.AMOLED_PURE,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        KronoThemeMode.AMOLED_PURE -> AmoledDarkColorScheme
        KronoThemeMode.DARK_NIGHT -> NightColorScheme
        KronoThemeMode.LIGHT_DAY -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
