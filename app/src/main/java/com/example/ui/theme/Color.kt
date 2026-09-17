package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Lavender & Purple Neumorphic Palette (from Reference Image)
val CanvasPurpleStart = Color(0xFF7568EB)
val CanvasPurpleMid = Color(0xFF867AF4)
val CanvasPurpleEnd = Color(0xFF988EFA)

val CardSurfaceLight = Color(0xFFF7F7FD)
val CardSurfaceWhite = Color(0xFFFFFFFF)
val CardSurfaceSubtle = Color(0xFFEEEEFA)

// Inset / Shadow Colors
val SoftShadowPurple = Color(0x38351F75)
val AmbientShadow = Color(0x24241656)
val CrispBorderLight = Color(0x40FFFFFF)
val SubtleCardBorder = Color(0x1A6753D8)

// Text Colors
val TextDark = Color(0xFF1E1B39)
val TextDarkSecondary = Color(0xFF5A557C)
val TextMuted = Color(0xFF8E88B4)
val TextLight = Color(0xFFFFFFFF)
val TextLightSecondary = Color(0xFFE2DFFC)

// Brand & Status Accents
val PrimaryPurple = Color(0xFF7061E7)
val PrimaryPurpleLight = Color(0xFF8F82F7)
val AccentLavender = Color(0xFFC7BFFB)
val AccentLavenderLight = Color(0xFFEDEAFD)

val SetupHighQuality = Color(0xFF10B981) // High Quality 80-100
val SetupModerate = Color(0xFFF59E0B)    // Moderate 60-79
val SetupWeak = Color(0xFFF97316)        // Weak 40-59
val SetupVeryWeak = Color(0xFFEA580C)    // Very Weak 20-39
val SetupDanger = Color(0xFFEF4444)      // Danger 0-19

val WinGreen = Color(0xFF10B981)
val LossRed = Color(0xFFEF4444)
val BreakevenGray = Color(0xFF6B7280)

// Dark Theme Colors
val DarkCanvasStart = Color(0xFF120E24)
val DarkCanvasEnd = Color(0xFF1A1434)
val DarkCardSurface = Color(0xFF231C44)
val DarkCardSurfaceElevated = Color(0xFF2C2455)
val DarkBorder = Color(0x26C7BFFB)

// Gradients
val PurpleCanvasGradient = Brush.verticalGradient(
    listOf(CanvasPurpleStart, CanvasPurpleMid, CanvasPurpleEnd)
)

val DarkCanvasGradient = Brush.verticalGradient(
    listOf(DarkCanvasStart, DarkCanvasEnd)
)

val PrimaryButtonGradient = Brush.horizontalGradient(
    listOf(PrimaryPurple, PrimaryPurpleLight)
)

val HighlightCardGradient = Brush.linearGradient(
    listOf(Color(0xFF887BF5), Color(0xFFA197FC))
)

val WinCardGradient = Brush.horizontalGradient(
    listOf(Color(0xFF059669), Color(0xFF10B981))
)

val DangerCardGradient = Brush.horizontalGradient(
    listOf(Color(0xFFDC2626), Color(0xFFEF4444))
)
