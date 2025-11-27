package com.neonoptic.app.domain.model

data class CameraSource(
    val id: String,
    val name: String,
    val type: CameraType,
    val protocol: ProtocolType,
    val address: String,
    val username: String? = null,
    val password: String? = null,
    val useTcp: Boolean = true,
    val bufferMs: Int = 750,
    val targetResolution: String? = null,
    val targetFps: Int? = null
)
