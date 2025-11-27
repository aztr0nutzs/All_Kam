package com.neonoptic.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neonoptic.app.ui.screens.DashboardScreen
import com.neonoptic.app.ui.screens.DashboardViewModel

object Routes {
    const val DASHBOARD = "dashboard"
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
            DashboardScreen(viewModel = viewModel)
        }
    }
}
