package com.neonoptic.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neonoptic.app.ui.theme.ElectricPurple
import com.neonoptic.app.ui.theme.Gunmetal
import com.neonoptic.app.ui.theme.NeonCyan

@Composable
fun CyberpunkCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    status: String? = null,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    val shape = RoundedCornerShape(12.dp)
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable { onClick() }
            .background(Gunmetal.copy(alpha = 0.85f)),
        shape = shape,
        color = Gunmetal.copy(alpha = 0.85f),
        shadowElevation = 12.dp,
        tonalElevation = 6.dp,
        border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(NeonCyan, Color.White, ElectricPurple)))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                    subtitle?.let {
                        Text(text = it, color = Color.LightGray)
                    }
                }
                status?.let {
                    StatusChip(text = it)
                }
            }
            content()
        }
    }
}

@Composable
private fun StatusChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = ElectricPurple.copy(alpha = 0.9f),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text.uppercase(), color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
