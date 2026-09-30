package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CloneAppEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloneSandboxRunnerDialog(
    clone: CloneAppEntity,
    onClose: () -> Unit
) {
    var activeTab by remember { mutableStateOf(0) }
    var chatMessage by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                "Welcome to Parallel Space Sandbox for ${clone.customName}!",
                "Chaos Engine active: Spoofed as ${clone.deviceBrand} ${clone.deviceModel}.",
                "Virtual Location: ${clone.virtualLocationName}."
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Android, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(clone.customName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Sandbox Instance #${clone.cloneIndex} • ${clone.deviceModel}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_sandbox_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close Sandbox")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )

            // Chaos engine telemetry bar
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Secure Parallel Sandbox", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Text("GPS: ${clone.virtualLocationName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                }
            }

            // Main Mock App Interface
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        messages.forEach { msg ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = msg,
                                    modifier = Modifier.padding(12.dp),
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Input bar inside cloned app
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatMessage,
                            onValueChange = { chatMessage = it },
                            placeholder = { Text("Test message inside clone...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sandbox_input_field"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (chatMessage.isNotEmpty()) {
                                    messages = messages + "You (Clone): $chatMessage"
                                    chatMessage = ""
                                }
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .testTag("sandbox_send_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }

            // Bottom Navigation Bar within Sandbox
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                    label = { Text("App Feed") },
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Tune, contentDescription = null) },
                    label = { Text("Chaos Stats") },
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.PrivacyTip, contentDescription = null) },
                    label = { Text("Privacy Vault") },
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 }
                )
            }
        }
    }
}
