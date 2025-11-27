package com.neonoptic.app.domain.repository

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.model.ConnectionStatus
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
    fun getCameras(): Flow<List<CameraSource>>
    suspend fun addOrUpdateCamera(camera: CameraSource)
    suspend fun removeCamera(id: String)
    fun testConnection(camera: CameraSource): Flow<ConnectionStatus>
}
