package com.neonoptic.app.domain.model

sealed class ConnectionStatus {
    object Connected : ConnectionStatus()
    object Connecting : ConnectionStatus()
    data class Failed(val reason: String) : ConnectionStatus()
}
