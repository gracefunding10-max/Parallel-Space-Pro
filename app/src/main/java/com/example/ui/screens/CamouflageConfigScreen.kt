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
import com.example.data.CloneAppEntity
import com.example.viewmodel.CloneViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamouflageConfigScreen(
    cloneId: Long,
    viewModel: CloneViewModel,
    onBack: () -> Unit
) {
    val clones by viewModel.allClones.collectAsState()
    val clone = clones.find { it.id == cloneId }

    if (clone == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Clone not found.")
        }
        return
    }

    var customName by remember { mutableStateOf(clone.customName) }
    var deviceBrand by remember { mutableStateOf(clone.deviceBrand) }
    var deviceModel by remember { mutableStateOf(clone.deviceModel) }
    var androidId by remember { mutableStateOf(clone.androidId) }
    var imei by remember { mutableStateOf(clone.imei) }
    var wifiMac by remember { mutableStateOf(clone.wifiMac) }
    var virtualLocation by remember { mutableStateOf(clone.virtualLocationName) }
    var hideSim by remember { mutableStateOf(clone.hideSim) }
    var hideGpu by remember { mutableStateOf(clone.hideGpu) }
    var preventRecording by remember { mutableStateOf(clone.preventScreenRecording) }
    var pinLock by remember { mutableStateOf(clone.pinLock ?: "") }
    var showSavedSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chaos & Camouflage: ${clone.appName}") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("config_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val updated = clone.copy(
                                customName = customName,
                                deviceBrand = deviceBrand,
                                deviceModel = deviceModel,
                                androidId = androidId,
                                imei = imei,
                                wifiMac = wifiMac,
                                virtualLocationName = virtualLocation,
                                hideSim = hideSim,
                                hideGpu = hideGpu,
                                preventScreenRecording = preventRecording,
                                pinLock = pinLock.ifEmpty { null }
                            )
                            viewModel.updateClone(updated)
                            showSavedSnackbar = true
                        },
                        modifier = Modifier.testTag("save_config_btn")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save Changes")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        snackbarHost = {
            if (showSavedSnackbar) {
                Snackbar(
                    action = {
                        TextButton(onClick = { showSavedSnackbar = false }) { Text("Dismiss") }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Camouflage profile updated successfully!")
                }
            }
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
            // General Info
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("General Settings", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Cloned App Display Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = pinLock,
                        onValueChange = { pinLock = it },
                        label = { Text("Security PIN Lock (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            // Device Camouflage
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Device Camouflage (Chaos Engine)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = deviceBrand,
                            onValueChange = { deviceBrand = it },
                            label = { Text("Brand") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = deviceModel,
                            onValueChange = { deviceModel = it },
                            label = { Text("Model") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = androidId,
                        onValueChange = { androidId = it },
                        label = { Text("Android ID") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = imei,
                        onValueChange = { imei = it },
                        label = { Text("IMEI / IMSI") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = wifiMac,
                        onValueChange = { wifiMac = it },
                        label = { Text("Wi-Fi / Bluetooth MAC Address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            // Privacy & Virtualization
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Privacy & Virtualization", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    OutlinedTextField(
                        value = virtualLocation,
                        onValueChange = { virtualLocation = it },
                        label = { Text("Virtual GPS Location") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hide SIM card & Carrier info", fontSize = 14.sp)
                        Switch(checked = hideSim, onCheckedChange = { hideSim = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hide GPU Fingerprint", fontSize = 14.sp)
                        Switch(checked = hideGpu, onCheckedChange = { hideGpu = it })
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Prevent background screen recording", fontSize = 14.sp)
                        Switch(checked = preventRecording, onCheckedChange = { preventRecording = it })
                    }
                }
            }

            Button(
                onClick = {
                    val updated = clone.copy(
                        customName = customName,
                        deviceBrand = deviceBrand,
                        deviceModel = deviceModel,
                        androidId = androidId,
                        imei = imei,
                        wifiMac = wifiMac,
                        virtualLocationName = virtualLocation,
                        hideSim = hideSim,
                        hideGpu = hideGpu,
                        preventScreenRecording = preventRecording,
                        pinLock = pinLock.ifEmpty { null }
                    )
                    viewModel.updateClone(updated)
                    showSavedSnackbar = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("bottom_save_config_btn")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Camouflage Configuration", fontSize = 16.sp)
            }
        }
    }
}
