package com.neonoptic.app.domain.usecase

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.repository.CameraRepository
import javax.inject.Inject

class AddCameraUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    suspend operator fun invoke(camera: CameraSource) {
        repository.addCamera(camera)
    }
}
