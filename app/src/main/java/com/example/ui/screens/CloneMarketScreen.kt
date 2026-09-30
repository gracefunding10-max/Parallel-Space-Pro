package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.CloneViewModel
import com.example.viewmodel.PresetAppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloneMarketScreen(
    viewModel: CloneViewModel,
    onBack: () -> Unit
) {
    var selectedPreset by remember { mutableStateOf<PresetAppInfo?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val presetApps = viewModel.presetApps

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Cloner Market") },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
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
            // Header banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Multi-Account Parallel Space",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Select any app below to clone with independent data storage, device spoofing & privacy isolation.",
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Text(
                text = "Popular Apps & Games for Cloning",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(presetApps) { preset ->
                    PresetAppGridItem(
                        preset = preset,
                        onCloneClick = {
                            selectedPreset = preset
                            showCreateDialog = true
                        }
                    )
                }
            }
        }
    }

    val currentPreset = selectedPreset
    if (showCreateDialog && currentPreset != null) {
        CreateCloneDialog(
            preset = currentPreset,
            onDismiss = { showCreateDialog = false },
            onConfirm = { customName, cloneIndex, pin, brand, model, locationName, lat, lng ->
                viewModel.createClone(
                    packageName = currentPreset.packageName,
                    appName = currentPreset.appName,
                    customName = customName,
                    cloneIndex = cloneIndex,
                    iconName = currentPreset.defaultIcon,
                    pinLock = pin,
                    deviceBrand = brand,
                    deviceModel = model,
                    androidId = java.util.UUID.randomUUID().toString().substring(0, 16),
                    imei = (100000000000000..999999999999999L).random().toString(),
                    wifiMac = "02:00:00:" + (10..99).random() + ":" + (10..99).random() + ":" + (10..99).random(),
                    virtualGpsLat = lat,
                    virtualGpsLng = lng,
                    virtualLocationName = locationName
                )
                showCreateDialog = false
                onBack()
            }
        )
    }
}

@Composable
fun PresetAppGridItem(
    preset: PresetAppInfo,
    onCloneClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCloneClick)
            .testTag("preset_item_${preset.packageName}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (preset.defaultIcon) {
                        "whatsapp" -> Icons.Default.Chat
                        "facebook" -> Icons.Default.Public
                        "instagram" -> Icons.Default.CameraAlt
                        "twitter" -> Icons.Default.Share
                        "telegram" -> Icons.Default.Send
                        "tiktok" -> Icons.Default.MusicNote
                        "freefire", "mobilelegends", "clashofclans" -> Icons.Default.SportsEsports
                        else -> Icons.Default.Android
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = preset.appName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = preset.category,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCloneClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Clone", fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCloneDialog(
    preset: PresetAppInfo,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String?, String, String, String, Double, Double) -> Unit
) {
    var customName by remember { mutableStateOf("${preset.appName} #${(2..5).random()}") }
    var cloneIndex by remember { mutableStateOf(2) }
    var pinLock by remember { mutableStateOf("") }
    var deviceBrand by remember { mutableStateOf("Google") }
    var deviceModel by remember { mutableStateOf("Pixel 9 Pro") }
    var locationName by remember { mutableStateOf("New York, NY") }
    var lat by remember { mutableStateOf(40.7128) }
    var lng by remember { mutableStateOf(-74.0060) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Clone ${preset.appName}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Cloned App Display Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_custom_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = deviceBrand,
                    onValueChange = { deviceBrand = it },
                    label = { Text("Camouflage Brand (e.g. Samsung)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = deviceModel,
                    onValueChange = { deviceModel = it },
                    label = { Text("Camouflage Model (e.g. Galaxy S24)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = { Text("Virtual GPS Location City") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = pinLock,
                    onValueChange = { pinLock = it },
                    label = { Text("Privacy PIN Lock (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        customName.ifEmpty { preset.appName },
                        cloneIndex,
                        pinLock.ifEmpty { null },
                        deviceBrand,
                        deviceModel,
                        locationName,
                        lat,
                        lng
                    )
                },
                modifier = Modifier.testTag("dialog_confirm_clone_btn")
            ) {
                Text("Start Cloning")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
