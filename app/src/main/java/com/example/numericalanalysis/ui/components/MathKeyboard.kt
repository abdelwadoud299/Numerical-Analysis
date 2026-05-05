package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MathKeyboard(
    onKeyClick: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keys = listOf(
        "7", "8", "9", "/", "sin",
        "4", "5", "6", "*", "cos",
        "1", "2", "3", "-", "exp",
        "0", ".", "x", "+", "log",
        "(", ")", "^", "sqrt", "DEL"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(280.dp)
            ) {
                items(keys) { key ->
                    KeyButton(
                        text = key,
                        onClick = {
                            if (key == "DEL") onBackspace()
                            else onKeyClick(if (key in listOf("sin", "cos", "exp", "log", "sqrt")) "$key(" else key)
                        },
                        isSpecial = key in listOf("/", "*", "-", "+", "^", "DEL"),
                        isFunction = key in listOf("sin", "cos", "exp", "log", "sqrt")
                    )
                }
            }
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    isSpecial: Boolean = false,
    isFunction: Boolean = false
) {
    val containerColor = when {
        text == "DEL" -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
        isSpecial -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        isFunction -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }
    
    val contentColor = when {
        text == "DEL" -> MaterialTheme.colorScheme.onErrorContainer
        isSpecial -> MaterialTheme.colorScheme.onPrimaryContainer
        isFunction -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = containerColor,
        contentColor = contentColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (text == "DEL") {
                Icon(Icons.Default.Backspace, contentDescription = null, modifier = Modifier.size(20.dp))
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isFunction) 12.sp else 16.sp
                    )
                )
            }
        }
    }
}
