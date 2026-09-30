package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.CloneAppEntity
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CloneViewModel

class MainActivity : ComponentActivity() {
    private val cloneViewModel: CloneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                var currentMainTab by remember { mutableStateOf(0) }
                var activeSandboxClone by remember { mutableStateOf<CloneAppEntity?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (activeSandboxClone == null) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Layers, contentDescription = null) },
                                    label = { Text("Clones") },
                                    selected = currentMainTab == 0,
                                    onClick = {
                                        currentMainTab = 0
                                        navController.navigate("home") { popUpTo("home") { inclusive = true } }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Memory, contentDescription = null) },
                                    label = { Text("Active Space") },
                                    selected = currentMainTab == 1,
                                    onClick = {
                                        currentMainTab = 1
                                        navController.navigate("active_space")
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                    label = { Text("Settings") },
                                    selected = currentMainTab == 2,
                                    onClick = {
                                        currentMainTab = 2
                                        navController.navigate("settings")
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    if (activeSandboxClone != null) {
                        CloneSandboxRunnerDialog(
                            clone = activeSandboxClone!!,
                            onClose = { activeSandboxClone = null }
                        )
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") {
                                HomeScreen(
                                    viewModel = cloneViewModel,
                                    onNavigateToMarket = { navController.navigate("market") },
                                    onNavigateToCamouflage = { cloneId -> navController.navigate("camouflage/$cloneId") },
                                    onLaunchSandbox = { clone -> activeSandboxClone = clone }
                                )
                            }
                            composable("market") {
                                CloneMarketScreen(
                                    viewModel = cloneViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("camouflage/{cloneId}") { backStackEntry ->
                                val cloneId = backStackEntry.arguments?.getString("cloneId")?.toLongOrNull() ?: 0L
                                CamouflageConfigScreen(
                                    cloneId = cloneId,
                                    viewModel = cloneViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("active_space") {
                                ActiveSpaceScreen(
                                    viewModel = cloneViewModel,
                                    onBack = { navController.popBackStack() },
                                    onLaunchSandbox = { clone -> activeSandboxClone = clone }
                                )
                            }
                            composable("settings") {
                                SettingsScreen(
                                    viewModel = cloneViewModel,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
