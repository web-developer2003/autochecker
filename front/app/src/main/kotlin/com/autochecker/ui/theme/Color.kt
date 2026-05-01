package com.autochecker.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Background
val DarkBackground = Color(0xFF0C0C0C)
val DarkSurface = Color(0xFF1A1A1A)
val DarkCard = Color(0xFF1E1E1E)
val DarkCardVariant = Color(0xFF252525)
val DarkDivider = Color(0xFF2A2A2A)

// Accent
val AccentRed = Color(0xFFFF3B30)
val AccentOrange = Color(0xFFFF6B35)

// Text
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0B0B0)
val TextTertiary = Color(0xFF808080)
val TextHint = Color(0xFF666666)

// Status
val StatusGreen = Color(0xFF34C759)
val StatusRed = Color(0xFFFF3B30)
val StatusYellow = Color(0xFFFFCC00)
val StatusGray = Color(0xFF8E8E93)

// Input
val InputBackground = Color(0xFF1A1A1A)
val InputBorder = Color(0xFF333333)
val InputBorderFocused = Color(0xFFFF3B30)

// Bottom Nav
val BottomNavBackground = Color(0xFF141414)
val BottomNavInactive = Color(0xFF666666)

@Immutable
data class AutoCheckerColors(
    val background: Color,
    val surface: Color,
    val card: Color,
    val cardVariant: Color,
    val divider: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textHint: Color,
    val inputBackground: Color,
    val inputBorder: Color,
    val inputBorderFocused: Color,
    val bottomNavBackground: Color,
    val bottomNavInactive: Color,
    val accentRed: Color,
    val accentOrange: Color,
    val statusGreen: Color,
    val statusRed: Color,
    val statusYellow: Color,
    val statusGray: Color,
)

val DarkAutoCheckerColors = AutoCheckerColors(
    background = DarkBackground,
    surface = DarkSurface,
    card = DarkCard,
    cardVariant = DarkCardVariant,
    divider = DarkDivider,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    textTertiary = TextTertiary,
    textHint = TextHint,
    inputBackground = InputBackground,
    inputBorder = InputBorder,
    inputBorderFocused = InputBorderFocused,
    bottomNavBackground = BottomNavBackground,
    bottomNavInactive = BottomNavInactive,
    accentRed = AccentRed,
    accentOrange = AccentOrange,
    statusGreen = StatusGreen,
    statusRed = StatusRed,
    statusYellow = StatusYellow,
    statusGray = StatusGray,
)

val LightAutoCheckerColors = AutoCheckerColors(
    background = Color(0xFFF5F5F7),
    surface = Color(0xFFFFFFFF),
    card = Color(0xFFFFFFFF),
    cardVariant = Color(0xFFF0F0F2),
    divider = Color(0xFFE5E5EA),
    textPrimary = Color(0xFF1C1C1E),
    textSecondary = Color(0xFF636366),
    textTertiary = Color(0xFF8E8E93),
    textHint = Color(0xFFAEAEB2),
    inputBackground = Color(0xFFFFFFFF),
    inputBorder = Color(0xFFD1D1D6),
    inputBorderFocused = AccentRed,
    bottomNavBackground = Color(0xFFFFFFFF),
    bottomNavInactive = Color(0xFF8E8E93),
    accentRed = AccentRed,
    accentOrange = AccentOrange,
    statusGreen = StatusGreen,
    statusRed = StatusRed,
    statusYellow = StatusYellow,
    statusGray = StatusGray,
)
