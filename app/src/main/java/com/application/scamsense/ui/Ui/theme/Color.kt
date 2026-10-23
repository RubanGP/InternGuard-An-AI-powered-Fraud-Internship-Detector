package com.application.scamsense.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.application.scamsense.model.RiskLevel

val Indigo = Color(0xFF4F46E5)
val Violet = Color(0xFF7C3AED)
val Mint = Color(0xFF10B981)
val Amber = Color(0xFFF59E0B)
val Rose = Color(0xFFEF4444)

val LightColors = lightColorScheme(
    primary = Indigo, onPrimary = Color.White, secondary = Violet,
    background = Color(0xFFF6F7FB), onBackground = Color(0xFF14162B),
    surface = Color.White, onSurface = Color(0xFF14162B),
    surfaceVariant = Color(0xFFE8EAF6), onSurfaceVariant = Color(0xFF5B5F7A),
    outline = Color(0xFFC9CCE0), error = Rose
)

val DarkColors = darkColorScheme(
    primary = Color(0xFF8B85FF), onPrimary = Color(0xFF14162B), secondary = Color(0xFFA78BFA),
    background = Color(0xFF0F1226), onBackground = Color(0xFFF1F2FA),
    surface = Color(0xFF1A1D3A), onSurface = Color(0xFFF1F2FA),
    surfaceVariant = Color(0xFF2A2E55), onSurfaceVariant = Color(0xFFA9ADCB),
    outline = Color(0xFF3A3F6B), error = Color(0xFFFF6B6B)
)

fun RiskLevel.color(): Color = when (this) {
    RiskLevel.LOW -> Mint
    RiskLevel.MEDIUM -> Amber
    RiskLevel.HIGH -> Rose
}
