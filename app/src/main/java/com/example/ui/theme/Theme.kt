package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LeanTrackLightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = MintSubtle,
    onPrimaryContainer = ForestGreenDark,
    secondary = SoftMint,
    onSecondary = Color.White,
    secondaryContainer = MintLight.copy(alpha = 0.35f),
    onSecondaryContainer = ForestGreenDark,
    tertiary = OrangeFlame,
    onTertiary = Color.White,
    background = WarmBackground,
    onBackground = Charcoal,
    surface = WarmSurface,
    onSurface = Charcoal,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = CharcoalMuted,
    outline = CardBorder,
    outlineVariant = ProgressTrack
)

private val LeanTrackDarkColorScheme = darkColorScheme(
    primary = MintLight,
    onPrimary = ForestGreenDark,
    primaryContainer = ForestGreenLight,
    onPrimaryContainer = Color.White,
    secondary = SoftMint,
    onSecondary = Color.White,
    secondaryContainer = ForestGreen,
    onSecondaryContainer = MintLight,
    tertiary = OrangeFlame,
    onTertiary = Color.White,
    background = Color(0xFF131713),
    onBackground = Color(0xFFE5E7E2),
    surface = Color(0xFF1B201B),
    onSurface = Color(0xFFE5E7E2),
    surfaceVariant = Color(0xFF242B24),
    onSurfaceVariant = Color(0xFFB0B5AC),
    outline = Color(0xFF384238),
    outlineVariant = Color(0xFF2C342C)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional LeanTrack brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LeanTrackDarkColorScheme else LeanTrackLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
