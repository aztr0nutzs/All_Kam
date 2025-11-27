package com.neonoptic.app.domain.usecase

import com.neonoptic.app.domain.repository.CameraRepository
import javax.inject.Inject

class RemoveCameraUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    suspend operator fun invoke(id: String) {
        repository.removeCamera(id)
    }
}
