package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun SettingsScreen(
    viewModel: CloneViewModel,
    onBack: () -> Unit
) {
    var masterLockEnabled by remember { mutableStateOf(false) }
    var backgroundKeepAlive by remember { mutableStateOf(true) }
    var virtualSdCard by remember { mutableStateOf(true) }
    var floatingKeyboard by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parallel Space Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_btn")) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Security & Privacy", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Master Security PIN Lock", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            Text("Protect access to Parallel Space Pro", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                        Switch(checked = masterLockEnabled, onCheckedChange = { masterLockEnabled = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Prevent Screen Recording", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            Text("Block background capture in clones", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                        Switch(checked = true, onCheckedChange = {})
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Engine & Performance", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Keep Running in Background", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            Text("Maintain active parallel state", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                        Switch(checked = backgroundKeepAlive, onCheckedChange = { backgroundKeepAlive = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Virtual SD Card Sandbox", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            Text("Isolate file storage per clone instance", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                        Switch(checked = virtualSdCard, onCheckedChange = { virtualSdCard = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Floating Virtual Keyboard", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                            Text("Encrypted input protection", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                        Switch(checked = floatingKeyboard, onCheckedChange = { floatingKeyboard = it })
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("About Parallel Space Pro", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Version 2.4.0 (Chaos Engine Enabled)", fontSize = 13.sp, color = MaterialTheme.colorScheme.outline)
                    Text("Zero personal data collected. All device camouflage parameters and virtual storage are computed locally within sandboxed isolation.", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
