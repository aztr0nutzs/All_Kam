package com.neonoptic.app.domain.usecase

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.repository.CameraRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetCameraUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    suspend operator fun invoke(id: String): CameraSource? {
        return repository.getCameras()
            .map { cameras -> cameras.firstOrNull { it.id == id } }
            .first()
    }
}
