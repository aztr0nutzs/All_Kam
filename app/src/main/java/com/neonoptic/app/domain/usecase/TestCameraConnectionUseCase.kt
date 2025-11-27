package com.neonoptic.app.domain.usecase

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.model.ConnectionStatus
import com.neonoptic.app.domain.repository.CameraRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class TestCameraConnectionUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    operator fun invoke(camera: CameraSource): Flow<ConnectionStatus> {
        return repository.testConnection(camera)
    }
}
