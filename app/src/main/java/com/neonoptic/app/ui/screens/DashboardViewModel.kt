package com.neonoptic.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neonoptic.app.domain.model.CameraSource
import com.neonoptic.app.domain.usecase.GetCamerasUseCase
import com.neonoptic.app.domain.usecase.RemoveCameraUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getCamerasUseCase: GetCamerasUseCase,
    private val removeCameraUseCase: RemoveCameraUseCase
) : ViewModel() {

    val cameraState: StateFlow<List<CameraSource>> = getCamerasUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeCamera(id: String) {
        viewModelScope.launch {
            removeCameraUseCase(id)
        }
    }
}
