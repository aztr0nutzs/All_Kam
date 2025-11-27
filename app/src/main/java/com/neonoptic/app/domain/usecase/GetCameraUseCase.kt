package com.neonoptic.app.domain.usecase

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.repository.CameraRepository
import javax.inject.Inject

class GetCameraUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    suspend operator fun invoke(id: String): CameraSource? = repository.getCamera(id)
}
