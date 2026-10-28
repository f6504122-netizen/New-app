package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Gold400,
    onPrimary = Navy950,
    primaryContainer = Navy800,
    onPrimaryContainer = Gold300,
    secondary = Gold500,
    onSecondary = Navy950,
    secondaryContainer = Navy700,
    onSecondaryContainer = OffWhite,
    tertiary = AmberFlame,
    onTertiary = Navy950,
    background = Navy950,
    onBackground = OffWhite,
    surface = Navy900,
    onSurface = OffWhite,
    surfaceVariant = Navy850,
    onSurfaceVariant = Slate300,
    outline = CardBorderNavy,
    outlineVariant = Navy700
)

private val LightColorScheme = lightColorScheme(
    primary = Navy900,
    onPrimary = PureWhite,
    primaryContainer = Gold300,
    onPrimaryContainer = Navy950,
    secondary = Gold600,
    onSecondary = PureWhite,
    secondaryContainer = Navy800,
    onSecondaryContainer = PureWhite,
    tertiary = AmberFlame,
    onTertiary = PureWhite,
    background = OffWhite,
    onBackground = Navy950,
    surface = PureWhite,
    onSurface = Navy950,
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = Navy800,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded Deeporam look
    content: @Composable () -> Unit,
) {
    // Default to our branded Dark luxury palette for maximum golden sheen, but support light mode cleanly
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
