package com.neonoptic.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neonoptic.app.ui.screens.CameraConfigScreen
import com.neonoptic.app.ui.screens.DashboardScreen
import com.neonoptic.app.ui.screens.DashboardViewModel

object Routes {
    const val DASHBOARD = "dashboard"
    const val CAMERA_CONFIG = "cameraConfig/{cameraId}"
}

@Composable
fun RootNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
        modifier = Modifier
    ) {
        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onCameraSelected = { camera ->
                    navController.navigate("cameraConfig/${camera.id}")
                },
                onAddCamera = { navController.navigate("cameraConfig/new") }
            )
        }
        composable(Routes.CAMERA_CONFIG) { backStackEntry ->
            val cameraId = backStackEntry.arguments?.getString("cameraId") ?: "new"
            CameraConfigScreen(cameraId = cameraId) {
                navController.popBackStack()
            }
        }
    }
}
