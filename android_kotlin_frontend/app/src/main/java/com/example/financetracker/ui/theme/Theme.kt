package com.example.financetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Core brand palette
private val Primary = Color(0xFF2563EB) // React primary blue
private val Secondary = Color(0xFFF59E0B) // Amber accent (kept for secondary UI)
private val Error = Color(0xFFEF4444) // React error red
private val Background = Color(0xFFF9FAFB)
private val Surface = Color(0xFFFFFFFF)
private val TextColor = Color(0xFF111827)

// Semantic palette for finances
private val IncomeGreen = Color(0xFF10B981) // Success green
private val ExpenseRed = Error

// Publicly expose semantic colors via a composition local for clarity
data class FinanceSemanticColors(
    val income: Color,
    val expense: Color
)

val LocalFinanceSemanticColors = staticCompositionLocalOf {
    FinanceSemanticColors(
        income = IncomeGreen,
        expense = ExpenseRed
    )
}

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
    // Force light theme as requested; provide semantic colors
    CompositionLocalProvider(
        LocalFinanceSemanticColors provides FinanceSemanticColors(
            income = IncomeGreen,
            expense = ExpenseRed
        )
    ) {
        MaterialTheme(
            colorScheme = LightColors,
            typography = AppTypography,
            content = content
        )
    }
}
