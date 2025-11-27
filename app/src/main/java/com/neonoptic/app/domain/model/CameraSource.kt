package com.neonoptic.app.domain.model

data class CameraSource(
    val id: String,
    val name: String,
    val type: CameraType,
    val address: String,
    val username: String? = null,
    val password: String? = null,
    val resolution: String? = null,
    val frameRate: Int? = null,
    val autoReconnect: Boolean = true
)
