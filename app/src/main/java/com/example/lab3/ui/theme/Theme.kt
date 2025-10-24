package com.example.lab3.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF82CFFF),
    onPrimary = Color.Black,
    surface = Color(0xFF111214),
    background = Color(0xFF0E1011),
    onSurface = Color(0xFFDDE1E6),
)

@Composable
fun Lab3Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography(),
        content = content
    )
}
