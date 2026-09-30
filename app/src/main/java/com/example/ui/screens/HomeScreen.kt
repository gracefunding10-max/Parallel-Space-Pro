package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CloneAppEntity
import com.example.viewmodel.CloneViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CloneViewModel,
    onNavigateToMarket: () -> Unit,
    onNavigateToCamouflage: (Long) -> Unit,
    onLaunchSandbox: (CloneAppEntity) -> Unit
) {
    val clones by viewModel.allClones.collectAsState()
    val runningCount = clones.count { it.isRunning }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = vmIconBoxModifier(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Layers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Parallel Space Pro", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("$runningCount Clones Active in Sandbox", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToMarket, modifier = Modifier.testTag("add_clone_top_button")) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Clone", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToMarket,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_clone")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Clone App")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Chaos Engine Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Chaos Engine Active",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Device ID, IMEI, GPS & Privacy spoofing enabled for sandbox isolation.",
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (clones.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Apps,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Clones Created Yet",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button below to clone WhatsApp, Games, or Social apps and run them in parallel space.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.widthIn(max = 280.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onNavigateToMarket,
                            modifier = Modifier.testTag("empty_add_clone_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create First Clone")
                        }
                    }
                }
            } else {
                Text(
                    text = "Cloned Applications (${clones.size})",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(clones, key = { it.id }) { clone ->
                        CloneAppCard(
                            clone = clone,
                            onToggleRunning = { viewModel.toggleRunning(clone) },
                            onLaunch = { onLaunchSandbox(clone) },
                            onConfigure = { onNavigateToCamouflage(clone.id) },
                            onDelete = { viewModel.deleteClone(clone) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun vmIconBoxModifier(containerColor: Color): Modifier {
    return Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(containerColor)
}

@Composable
fun CloneAppCard(
    clone: CloneAppEntity,
    onToggleRunning: () -> Unit,
    onLaunch: () -> Unit,
    onConfigure: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("clone_card_${clone.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Icon Box
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (clone.iconName) {
                            "whatsapp" -> Icons.Default.Chat
                            "facebook" -> Icons.Default.Public
                            "instagram" -> Icons.Default.CameraAlt
                            "game" -> Icons.Default.SportsEsports
                            else -> Icons.Default.Android
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = clone.customName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Badge(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text("#${clone.cloneIndex}", fontSize = 10.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Original: ${clone.appName} • ${clone.deviceBrand} ${clone.deviceModel}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (clone.isRunning) Color(0xFF4CAF50) else Color(0xFF9E9E9E))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (clone.isRunning) "Running (${clone.ramUsageMb} MB RAM)" else "Stopped",
                            fontSize = 11.sp,
                            color = if (clone.isRunning) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outline
                        )
                    }
                }

                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Launch Sandbox") },
                        onClick = {
                            showMenu = false
                            onLaunch()
                        },
                        leadingIcon = { Icon(Icons.Default.PlayArrow, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Chaos & Camouflage") },
                        onClick = {
                            showMenu = false
                            onConfigure()
                        },
                        leadingIcon = { Icon(Icons.Default.Tune, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Clone") },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(clone.virtualLocationName, fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onConfigure,
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("Config", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            if (!clone.isRunning) {
                                onToggleRunning()
                            }
                            onLaunch()
                        },
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("launch_btn_${clone.id}"),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (clone.isRunning) "Open" else "Start", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
