package com.neonoptic.app.domain.usecase

import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.repository.CameraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCamerasUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    operator fun invoke(): Flow<List<CameraSource>> = repository.getCameras()
}
