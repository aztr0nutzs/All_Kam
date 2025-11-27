package com.neonoptic.app.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnvifClient @Inject constructor() {
    suspend fun discoverDevices(): List<String> = withContext(Dispatchers.IO) {
        // Stub discovery uses SSDP/HTTP in production; here we return an empty list to keep I/O non-blocking.
        emptyList()
    }

    suspend fun fetchCapabilities(endpoint: String): Map<String, String> = withContext(Dispatchers.IO) {
        // Future implementation will parse ONVIF GetCapabilities responses.
        emptyMap()
    }
}
