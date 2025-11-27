package com.neonoptic.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    secondary = ElectricPurple,
    onSecondary = Color.Black,
    background = Gunmetal,
    surface = NeonBlack,
    onBackground = NeonCyan,
    onSurface = NeonCyan,
    outline = ElectricPurple
)

@Composable
fun NeonOpticTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
