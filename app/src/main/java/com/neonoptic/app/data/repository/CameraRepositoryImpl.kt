package com.neonoptic.app.data.repository

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.model.ConnectionStatus
import com.neonoptic.app.domain.model.ProtocolType
import com.neonoptic.app.domain.repository.CameraRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Singleton
class CameraRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CameraRepository {

    private val cameras = MutableStateFlow<List<CameraSource>>(emptyList())

    override fun getCameras(): Flow<List<CameraSource>> = cameras.asStateFlow()

    override suspend fun addOrUpdateCamera(camera: CameraSource) {
        withContext(ioDispatcher) {
            val updated = cameras.value.toMutableList().apply {
                val existingIndex = indexOfFirst { it.id == camera.id }
                if (existingIndex >= 0) {
                    this[existingIndex] = camera
                } else {
                    add(camera)
                }
            }
            cameras.value = updated
        }
    }

    override suspend fun removeCamera(id: String) {
        withContext(ioDispatcher) {
            cameras.value = cameras.value.filterNot { it.id == id }
        }
    }

    override fun testConnection(camera: CameraSource): Flow<ConnectionStatus> = flow {
        emit(ConnectionStatus.Connecting)
        delay(650)
        val hasAddress = camera.address.isNotBlank()
        val protocolSupported = when (camera.protocol) {
            ProtocolType.RTSP, ProtocolType.ONVIF, ProtocolType.HTTP_MJPEG, ProtocolType.USB_UVC -> true
        }
        if (hasAddress && protocolSupported) {
            emit(ConnectionStatus.Connected)
        } else {
            emit(ConnectionStatus.Error("Invalid camera configuration"))
        }
    }.flowOn(ioDispatcher)
}
