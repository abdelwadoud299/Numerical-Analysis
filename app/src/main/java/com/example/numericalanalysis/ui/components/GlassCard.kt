package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.numericalanalysis.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val settings = LocalAppSettings.current
    val isDark = settings.isDarkMode
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (isDark) GlassBackgroundDark else Color.White,
        shape = RoundedCornerShape(cornerRadius),
        border = BorderStroke(1.dp, if (isDark) GlassBorderDark else Color(0xFFF1F5F9)),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}
