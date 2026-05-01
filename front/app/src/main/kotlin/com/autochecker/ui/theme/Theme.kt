package com.autochecker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentRed,
    secondary = AccentOrange,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkCard,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = InputBorder,
    outlineVariant = DarkDivider,
    error = StatusRed,
)

private val LightColorScheme = lightColorScheme(
    primary = AccentRed,
    secondary = AccentOrange,
    background = Color(0xFFF5F5F7),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1C1E),
    onSurface = Color(0xFF1C1C1E),
    onSurfaceVariant = Color(0xFF636366),
    outline = Color(0xFFD1D1D6),
    outlineVariant = Color(0xFFE5E5EA),
    error = StatusRed,
)

val LocalAutoCheckerColors = staticCompositionLocalOf { DarkAutoCheckerColors }

@Composable
fun AutoCheckerTheme(
    isDarkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val autoCheckerColors = if (isDarkTheme) DarkAutoCheckerColors else LightAutoCheckerColors

    CompositionLocalProvider(LocalAutoCheckerColors provides autoCheckerColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AutoCheckerTypography,
            shapes = AutoCheckerShapes,
            content = content
        )
    }
}

object AutoCheckerTheme {
    val colors: AutoCheckerColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAutoCheckerColors.current
}
