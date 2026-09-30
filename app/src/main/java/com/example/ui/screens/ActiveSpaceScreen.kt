package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.CloneViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSpaceScreen(
    viewModel: CloneViewModel,
    onBack: () -> Unit,
    onLaunchSandbox: (com.example.data.CloneAppEntity) -> Unit
) {
    val clones by viewModel.allClones.collectAsState()
    val runningClones = clones.filter { it.isRunning }
    val totalRam = runningClones.sumOf { it.ramUsageMb }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Parallel Space") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("active_space_back")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Stats Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Sandbox Memory", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        Text("$totalRam MB", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Column {
                        Text("Active Clones", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        Text("${runningClones.size} / ${clones.size}", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }

            if (runningClones.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Memory, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Clones Currently Running", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Start a cloned app from the home screen to run in parallel space.", fontSize = 13.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                Text(
                    text = "Running Instances",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(runningClones, key = { it.id }) { clone ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(clone.customName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("RAM: ${clone.ramUsageMb} MB • Model: ${clone.deviceModel}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { viewModel.toggleRunning(clone) },
                                        modifier = Modifier.height(32.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text("Stop", fontSize = 12.sp)
                                    }
                                    Button(
                                        onClick = { onLaunchSandbox(clone) },
                                        modifier = Modifier.height(32.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Text("Open", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
