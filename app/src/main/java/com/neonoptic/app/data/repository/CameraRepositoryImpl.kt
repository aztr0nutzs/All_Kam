package com.neonoptic.app.data.repository

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.model.CameraType
import com.neonoptic.app.domain.model.ConnectionStatus
import com.neonoptic.app.domain.repository.CameraRepository
import com.neonoptic.app.network.OnvifClient
import com.neonoptic.app.usb.UsbCameraManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.videolan.libvlc.LibVLC
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraRepositoryImpl @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val libVlc: LibVLC,
    private val usbCameraManager: UsbCameraManager,
    private val onvifClient: OnvifClient
) : CameraRepository {

    private val cameras = MutableStateFlow<List<CameraSource>>(emptyList())

    override fun getCameras(): Flow<List<CameraSource>> = cameras.asStateFlow()

    override suspend fun getCamera(id: String): CameraSource? = withContext(Dispatchers.IO) {
        cameras.value.firstOrNull { it.id == id }
    }

    override suspend fun addCamera(camera: CameraSource) {
        cameras.value = cameras.value.filterNot { it.id == camera.id } + camera
    }

    override suspend fun removeCamera(id: String) {
        cameras.value = cameras.value.filterNot { it.id == id }
    }

    override suspend fun testConnection(camera: CameraSource): ConnectionStatus =
        withContext(Dispatchers.IO) {
            when (camera.type) {
                CameraType.IP -> testIpCamera(camera)
                CameraType.USB -> testUsbCamera(camera)
            }
        }

    private fun testIpCamera(camera: CameraSource): ConnectionStatus {
        return try {
            val mediaItem = MediaItem.Builder()
                .setUri(Uri.parse(camera.address))
                .setMediaId(camera.id)
                .build()
            // ExoPlayer is the primary RTSP client for low-latency playback.
            exoPlayer.setMediaItem(mediaItem)
            // The actual prepare/playback happens in the player UI; here we just ensure the URI is parsable.
            exoPlayer.prepare()
            runCatching { onvifClient.fetchCapabilities(camera.address) }
            ConnectionStatus.Connected
        } catch (playerError: Exception) {
            // For non-standard RTSP endpoints, fall back to LibVLC which is tolerant of more transport variations.
            try {
                libVlc.version // Access to confirm the native library is loaded.
                ConnectionStatus.Connecting
            } catch (vlcError: Exception) {
                ConnectionStatus.Failed(playerError.message ?: "Unknown RTSP error")
            }
        }
    }

    private fun testUsbCamera(camera: CameraSource): ConnectionStatus {
        val devices = usbCameraManager.listUsbDevices()
        val matchingDevice = devices.firstOrNull { it.deviceId == camera.address }
        return if (matchingDevice != null) {
            ConnectionStatus.Connected
        } else {
            ConnectionStatus.Failed("USB device not detected")
        }
    }
}
