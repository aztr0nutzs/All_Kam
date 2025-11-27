package com.neonoptic.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.ui.components.CyberpunkCard
import com.neonoptic.app.ui.theme.ElectricPurple
import com.neonoptic.app.ui.theme.Gunmetal
import com.neonoptic.app.ui.theme.NeonCyan

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel
) {
    val cameras by viewModel.cameras.collectAsState()

    Scaffold(
        topBar = {
            DashboardTopBar(onAddCamera = viewModel::onAddDummyCamera)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onAddDummyCamera,
                containerColor = ElectricPurple,
                contentColor = Color.Black
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Camera")
            }
        },
        containerColor = Gunmetal
    ) { padding ->
        DashboardGridContent(
            cameras = cameras,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Gunmetal)
        )
    }
}

@Composable
private fun DashboardTopBar(onAddCamera: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "NEONOPTIC",
                color = NeonCyan,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        },
        actions = {
            CyberpunkTopAction(onAddCamera)
        }
    )
}

@Composable
private fun CyberpunkTopAction(onAddCamera: () -> Unit) {
    CyberpunkCard(
        modifier = Modifier
            .padding(end = 12.dp),
        title = "+ Camera",
        onClick = onAddCamera
    )
}

@Composable
private fun DashboardGridContent(
    cameras: List<CameraSource>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 92.dp)
    ) {
        items(items = cameras, key = { it.id }) { camera ->
            CameraTile(cameraSource = camera)
        }
    }
}

@Composable
private fun CameraTile(cameraSource: CameraSource) {
    CyberpunkCard(
        modifier = Modifier.fillMaxWidth(),
        title = cameraSource.name,
        subtitle = "${cameraSource.type} • ${cameraSource.protocol}",
        status = "Idle",
        onClick = {}
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = cameraSource.address,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            cameraSource.targetResolution?.let { resolution ->
                Text(
                    text = "Resolution: $resolution",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
            cameraSource.targetFps?.let { fps ->
                Text(
                    text = "Target FPS: $fps",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}
