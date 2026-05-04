package com.example.numericalanalysis.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

data class AppSettings(
    val isDarkMode: Boolean = false,
    val accentColor: Color = BrandBlue,
    val precision: Int = 4
)

val LocalAppSettings = staticCompositionLocalOf { AppSettings() }

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    background = Background,
    surface = Surface,
    onBackground = OnBackground,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline
)

@Composable
fun NumericalAnalysisTheme(
    settings: AppSettings = AppSettings(),
    content: @Composable () -> Unit
) {
    val darkTheme = settings.isDarkMode
    val accentColor = settings.accentColor
    
    val colorScheme = if (darkTheme) {
        DarkColorScheme.copy(
            primary = accentColor,
            onPrimary = if (accentColor == BrandOrange) Color.Black else Color.White,
            primaryContainer = accentColor.copy(alpha = 0.2f),
            onPrimaryContainer = accentColor
        )
    } else {
        LightColorScheme.copy(
            primary = accentColor,
            onPrimary = Color.White,
            primaryContainer = accentColor.copy(alpha = 0.1f),
            onPrimaryContainer = accentColor
        )
    }

    CompositionLocalProvider(LocalAppSettings provides settings) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}