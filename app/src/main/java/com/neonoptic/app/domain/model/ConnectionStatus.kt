package com.neonoptic.app.domain.model

sealed class ConnectionStatus {
    object Idle : ConnectionStatus()
    object Connecting : ConnectionStatus()
    object Connected : ConnectionStatus()
    data class Error(val message: String) : ConnectionStatus()
}
