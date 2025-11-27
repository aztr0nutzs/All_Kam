package com.neonoptic.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.model.CameraType
import com.neonoptic.app.domain.model.ProtocolType
import com.neonoptic.app.domain.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {

    val cameras: StateFlow<List<CameraSource>> = cameraRepository.getCameras()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onAddDummyCamera() {
        viewModelScope.launch {
            val existing = cameras.value
            val nextIndex = existing.size + 1
            val ipCamera = CameraSource(
                id = UUID.randomUUID().toString(),
                name = "Neon IP Cam $nextIndex",
                type = CameraType.IP,
                protocol = ProtocolType.RTSP,
                address = "rtsp://192.168.0.${10 + nextIndex}:554/stream",
                username = "admin",
                password = "neonoptic",
                useTcp = true,
                bufferMs = 650,
                targetResolution = "1920x1080",
                targetFps = 30
            )
            val usbCamera = CameraSource(
                id = UUID.randomUUID().toString(),
                name = "USB Vision $nextIndex",
                type = CameraType.USB,
                protocol = ProtocolType.USB_UVC,
                address = "USB_DEVICE_$nextIndex",
                useTcp = false,
                bufferMs = 250,
                targetResolution = "1280x720",
                targetFps = 60
            )
            cameraRepository.addOrUpdateCamera(ipCamera)
            cameraRepository.addOrUpdateCamera(usbCamera)
        }
    }

    fun removeCamera(id: String) {
        viewModelScope.launch {
            cameraRepository.removeCamera(id)
        }
    }
}
