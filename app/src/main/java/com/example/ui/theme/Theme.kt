package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurpleLight,
    onPrimary = Color.White,
    primaryContainer = DarkCardSurfaceElevated,
    onPrimaryContainer = AccentLavender,
    secondary = AccentLavender,
    onSecondary = DarkCanvasStart,
    background = DarkCanvasStart,
    onBackground = TextLight,
    surface = DarkCardSurface,
    onSurface = TextLight,
    surfaceVariant = DarkCardSurfaceElevated,
    onSurfaceVariant = TextLightSecondary,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = AccentLavenderLight,
    onPrimaryContainer = PrimaryPurple,
    secondary = PrimaryPurpleLight,
    onSecondary = Color.White,
    background = CanvasPurpleStart,
    onBackground = TextLight,
    surface = CardSurfaceLight,
    onSurface = TextDark,
    surfaceVariant = CardSurfaceSubtle,
    onSurfaceVariant = TextDarkSecondary,
    outline = SubtleCardBorder
)

@Composable
fun TradeScoreTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
