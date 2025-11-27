package com.neonoptic.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.model.CameraType
import com.neonoptic.app.domain.model.ConnectionStatus
import com.neonoptic.app.domain.model.ProtocolType
import com.neonoptic.app.domain.usecase.AddOrUpdateCameraUseCase
import com.neonoptic.app.domain.usecase.GetCameraUseCase
import com.neonoptic.app.domain.usecase.TestCameraConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CameraConfigViewModel @Inject constructor(
    private val addOrUpdateCameraUseCase: AddOrUpdateCameraUseCase,
    private val testCameraConnectionUseCase: TestCameraConnectionUseCase,
    private val getCameraUseCase: GetCameraUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(CameraFormState())
    val formState: StateFlow<CameraFormState> = _formState

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Idle)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus

    fun prepare(cameraId: String) {
        if (cameraId == "new") {
            if (_formState.value.id == null) {
                _formState.value = _formState.value.copy(id = UUID.randomUUID().toString())
            }
            return
        }
        viewModelScope.launch {
            val existing = getCameraUseCase(cameraId)
            existing?.let { camera ->
                _formState.value = CameraFormState(
                    id = camera.id,
                    name = camera.name,
                    address = camera.address,
                    username = camera.username.orEmpty(),
                    password = camera.password.orEmpty(),
                    type = camera.type,
                    protocol = camera.protocol,
                    useTcp = camera.useTcp,
                    bufferMs = camera.bufferMs,
                    targetResolution = camera.targetResolution.orEmpty(),
                    targetFps = camera.targetFps?.toString().orEmpty()
                )
            }
        }
    }

    fun updateName(value: String) {
        _formState.value = _formState.value.copy(name = value)
    }

    fun updateAddress(value: String) {
        _formState.value = _formState.value.copy(address = value)
    }

    fun updateUsername(value: String) {
        _formState.value = _formState.value.copy(username = value)
    }

    fun updatePassword(value: String) {
        _formState.value = _formState.value.copy(password = value)
    }

    fun updateType(value: CameraType) {
        _formState.value = _formState.value.copy(
            type = value,
            protocol = defaultProtocolFor(value)
        )
    }

    fun saveCamera() {
        val current = _formState.value
        val validationError = when {
            current.name.isBlank() -> "Please enter a camera name"
            current.address.isBlank() -> "Please provide the RTSP/HTTP URL or USB device id"
            else -> null
        }
        if (validationError != null) {
            _formState.value = current.copy(validationError = validationError)
            return
        }
        viewModelScope.launch {
            val camera = current.toCameraSource()
            addOrUpdateCameraUseCase(camera)
            _formState.value = current.copy(validationError = null)
        }
    }

    fun testConnection() {
        val camera = _formState.value.toCameraSource()
        viewModelScope.launch {
            testCameraConnectionUseCase(camera).collect { status ->
                _connectionStatus.value = status
            }
        }
    }

    private fun defaultProtocolFor(type: CameraType): ProtocolType {
        return when (type) {
            CameraType.IP -> ProtocolType.RTSP
            CameraType.USB -> ProtocolType.USB_UVC
        }
    }

    private fun CameraFormState.toCameraSource(): CameraSource {
        return CameraSource(
            id = id ?: UUID.randomUUID().toString(),
            name = name,
            type = type,
            protocol = protocol,
            address = address,
            username = username.ifBlank { null },
            password = password.ifBlank { null },
            useTcp = useTcp,
            bufferMs = bufferMs,
            targetResolution = targetResolution.ifBlank { null },
            targetFps = targetFps.toIntOrNull()
        )
    }
}

data class CameraFormState(
    val id: String? = null,
    val name: String = "",
    val address: String = "",
    val username: String = "",
    val password: String = "",
    val type: CameraType = CameraType.IP,
    val protocol: ProtocolType = ProtocolType.RTSP,
    val useTcp: Boolean = true,
    val bufferMs: Int = 750,
    val targetResolution: String = "",
    val targetFps: String = "",
    val validationError: String? = null
)
