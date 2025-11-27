package com.neonoptic.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neonoptic.app.ui.theme.Gunmetal
import com.neonoptic.app.ui.theme.NeonCyan

@Composable
fun CyberpunkCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Surface(
        modifier = modifier
            .clip(shape)
            .background(Gunmetal.copy(alpha = 0.75f)),
        shape = shape,
        color = Gunmetal.copy(alpha = 0.75f),
        shadowElevation = 10.dp,
        tonalElevation = 6.dp,
        border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(NeonCyan, Color.White, NeonCyan)))
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
