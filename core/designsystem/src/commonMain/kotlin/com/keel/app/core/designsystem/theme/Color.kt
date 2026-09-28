package com.keel.app.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal object KeelPalette {
    val Navy10 = Color(0xFF001B3D)
    val Navy20 = Color(0xFF003063)
    val Navy30 = Color(0xFF00468C)
    val Navy40 = Color(0xFF1E5DA8)
    val Navy80 = Color(0xFFA8C8FF)
    val Navy90 = Color(0xFFD5E3FF)
    val Teal30 = Color(0xFF004F4F)
    val Teal40 = Color(0xFF006A6A)
    val Teal80 = Color(0xFF4CDADA)
    val Teal90 = Color(0xFF6FF7F6)
    val Red40 = Color(0xFFBA1A1A)
    val Red80 = Color(0xFFFFB4AB)
    val Red90 = Color(0xFFFFDAD6)
    val Red20 = Color(0xFF690005)
    val Neutral10 = Color(0xFF1A1C1E)
    val Neutral90 = Color(0xFFE3E2E6)
    val Neutral95 = Color(0xFFF1F0F4)
    val Neutral99 = Color(0xFFFDFCFF)
    val NeutralVariant30 = Color(0xFF43474E)
    val NeutralVariant50 = Color(0xFF74777F)
    val NeutralVariant80 = Color(0xFFC4C6CF)
    val NeutralVariant90 = Color(0xFFE0E2EC)
}

internal val LightColorScheme = lightColorScheme(
    primary = KeelPalette.Navy40,
    onPrimary = Color.White,
    primaryContainer = KeelPalette.Navy90,
    onPrimaryContainer = KeelPalette.Navy10,
    secondary = KeelPalette.Teal40,
    onSecondary = Color.White,
    secondaryContainer = KeelPalette.Teal90,
    onSecondaryContainer = KeelPalette.Teal30,
    error = KeelPalette.Red40,
    onError = Color.White,
    errorContainer = KeelPalette.Red90,
    background = KeelPalette.Neutral99,
    onBackground = KeelPalette.Neutral10,
    surface = KeelPalette.Neutral99,
    onSurface = KeelPalette.Neutral10,
    surfaceVariant = KeelPalette.NeutralVariant90,
    onSurfaceVariant = KeelPalette.NeutralVariant30,
    outline = KeelPalette.NeutralVariant50,
)

internal val DarkColorScheme = darkColorScheme(
    primary = KeelPalette.Navy80,
    onPrimary = KeelPalette.Navy20,
    primaryContainer = KeelPalette.Navy30,
    onPrimaryContainer = KeelPalette.Navy90,
    secondary = KeelPalette.Teal80,
    onSecondary = KeelPalette.Teal30,
    secondaryContainer = KeelPalette.Teal30,
    onSecondaryContainer = KeelPalette.Teal90,
    error = KeelPalette.Red80,
    onError = KeelPalette.Red20,
    errorContainer = KeelPalette.Red40,
    background = KeelPalette.Neutral10,
    onBackground = KeelPalette.Neutral90,
    surface = KeelPalette.Neutral10,
    onSurface = KeelPalette.Neutral90,
    surfaceVariant = KeelPalette.NeutralVariant30,
    onSurfaceVariant = KeelPalette.NeutralVariant80,
    outline = KeelPalette.NeutralVariant50,
)
