package com.neonoptic.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neonoptic.app.domain.model.CameraType
import com.neonoptic.app.domain.model.ConnectionStatus
import com.neonoptic.app.ui.components.CyberpunkCard

@Composable
fun CameraConfigScreen(
    cameraId: String,
    onFinished: () -> Unit,
    viewModel: CameraConfigViewModel = hiltViewModel()
) {
    val status = viewModel.connectionStatus.collectAsStateWithLifecycle().value
    val formState = viewModel.formState.collectAsStateWithLifecycle().value
    val expandedState = remember { mutableStateOf(false) }

    LaunchedEffect(cameraId) {
        viewModel.prepare(cameraId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CyberpunkCard(
            modifier = Modifier.fillMaxWidth(),
            title = "Configure Camera",
            onClick = {}
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Configure Camera", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = formState.name,
                    onValueChange = viewModel::updateName,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = formState.address,
                    onValueChange = viewModel::updateAddress,
                    label = { Text("RTSP/HTTP URL or USB ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = formState.username,
                    onValueChange = viewModel::updateUsername,
                    label = { Text("Username (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = formState.password,
                    onValueChange = viewModel::updatePassword,
                    label = { Text("Password (optional)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenuField(
                    expanded = expandedState.value,
                    selectedType = formState.type,
                    onExpandChanged = { expandedState.value = it },
                    onTypeSelected = viewModel::updateType
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.saveCamera()
                    if (viewModel.formState.value.validationError == null) {
                        onFinished()
                    }
                }) {
                    Text("Save")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = viewModel::testConnection) {
                    Text("Test Connection")
                }
                formState.validationError?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (status) {
                        ConnectionStatus.Connected -> "Connection successful"
                        ConnectionStatus.Connecting -> "Connecting..."
                        ConnectionStatus.Idle -> "Idle"
                        is ConnectionStatus.Error -> "Connection failed: ${status.message}"
                    },
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onFinished) {
            Text("Back")
        }
    }
}

@Composable
private fun DropdownMenuField(
    expanded: Boolean,
    selectedType: CameraType,
    onExpandChanged: (Boolean) -> Unit,
    onTypeSelected: (CameraType) -> Unit
) {
    Column {
        Text(text = "Camera Type", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = { onExpandChanged(true) }) {
            Text(text = selectedType.name)
            IconButton(onClick = { onExpandChanged(!expanded) }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select type")
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandChanged(false) }) {
            CameraType.values().forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onTypeSelected(type)
                        onExpandChanged(false)
                    }
                )
            }
        }
    }
}
