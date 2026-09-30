package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cloned_apps")
data class CloneAppEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val packageName: String,
    val appName: String,
    val customName: String,
    val cloneIndex: Int,
    val iconName: String,
    val pinLock: String? = null,
    val windowMode: Boolean = false,
    val keepRunningInBackground: Boolean = true,
    val googleServicesEnabled: Boolean = true,
    val deviceBrand: String = "Samsung",
    val deviceModel: String = "Galaxy S24 Ultra",
    val androidId: String = "89a4b2c1e7f36d50",
    val imei: String = "864290041829375",
    val wifiMac: String = "02:00:00:00:00:01",
    val bluetoothMac: String = "02:00:00:00:00:02",
    val googleAdId: String = "f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
    val webviewProxy: String = "",
    val hideSim: Boolean = true,
    val hideGpu: Boolean = false,
    val virtualGpsLat: Double = 37.7749,
    val virtualGpsLng: Double = -122.4194,
    val virtualLocationName: String = "San Francisco, CA",
    val virtualPhotoCount: Int = 120,
    val virtualContactCount: Int = 45,
    val preventScreenRecording: Boolean = true,
    val isRunning: Boolean = false,
    val ramUsageMb: Int = 148,
    val createdAt: Long = System.currentTimeMillis()
)
