package com.example.financetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Primary = Color(0xFF2563EB)
private val Secondary = Color(0xFFF59E0B) // success
private val Error = Color(0xFFEF4444)
private val Background = Color(0xFFF9FAFB)
private val Surface = Color(0xFFFFFFFF)
private val TextColor = Color(0xFF111827)

private val LightColors: ColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    secondary = Secondary,
    onSecondary = Color(0xFF111827),
    error = Error,
    onError = Color.White,
    background = Background,
    onBackground = TextColor,
    surface = Surface,
    onSurface = TextColor,
)

private val AppTypography = Typography()

// PUBLIC_INTERFACE
@Composable
fun FinanceTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Force light theme as requested
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
