package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CloneAppEntity
import com.example.data.CloneRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PresetAppInfo(
    val packageName: String,
    val appName: String,
    val defaultIcon: String,
    val category: String
)

class CloneViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CloneRepository

    val allClones: StateFlow<List<CloneAppEntity>>

    val presetApps = listOf(
        PresetAppInfo("com.whatsapp", "WhatsApp", "whatsapp", "Social"),
        PresetAppInfo("com.facebook.katana", "Facebook", "facebook", "Social"),
        PresetAppInfo("com.instagram.android", "Instagram", "instagram", "Social"),
        PresetAppInfo("com.twitter.android", "X (Twitter)", "twitter", "Social"),
        PresetAppInfo("com.telegram.messenger", "Telegram", "telegram", "Social"),
        PresetAppInfo("com.zhiliaoapp.musically", "TikTok", "tiktok", "Entertainment"),
        PresetAppInfo("com.dts.freefireth", "Free Fire", "freefire", "Game"),
        PresetAppInfo("com.mobile.legends", "Mobile Legends", "mobilelegends", "Game"),
        PresetAppInfo("com.supercell.clashofclans", "Clash of Clans", "clashofclans", "Game"),
        PresetAppInfo("com.line.corp", "LINE Messenger", "line", "Social")
    )

    init {
        val cloneDao = AppDatabase.getDatabase(application).cloneDao()
        repository = CloneRepository(cloneDao)
        allClones = repository.allClones.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial clones if empty
        viewModelScope.launch {
            // We check via collection or direct query in repository if needed, or insert defaults if list is empty on first run
        }
    }

    fun seedDefaultClonesIfNeeded() {
        viewModelScope.launch {
            // If repository list is empty, add WhatsApp and Facebook
            // We can check inside repository or collect once
        }
    }

    fun createClone(
        packageName: String,
        appName: String,
        customName: String,
        cloneIndex: Int,
        iconName: String,
        pinLock: String?,
        deviceBrand: String,
        deviceModel: String,
        androidId: String,
        imei: String,
        wifiMac: String,
        virtualGpsLat: Double,
        virtualGpsLng: Double,
        virtualLocationName: String
    ) {
        viewModelScope.launch {
            val entity = CloneAppEntity(
                packageName = packageName,
                appName = appName,
                customName = customName,
                cloneIndex = cloneIndex,
                iconName = iconName,
                pinLock = pinLock,
                deviceBrand = deviceBrand,
                deviceModel = deviceModel,
                androidId = androidId,
                imei = imei,
                wifiMac = wifiMac,
                virtualGpsLat = virtualGpsLat,
                virtualGpsLng = virtualGpsLng,
                virtualLocationName = virtualLocationName,
                isRunning = false,
                ramUsageMb = (120..280).random()
            )
            repository.insertClone(entity)
        }
    }

    fun updateClone(clone: CloneAppEntity) {
        viewModelScope.launch {
            repository.updateClone(clone)
        }
    }

    fun deleteClone(clone: CloneAppEntity) {
        viewModelScope.launch {
            repository.deleteClone(clone)
        }
    }

    fun toggleRunning(clone: CloneAppEntity) {
        viewModelScope.launch {
            val newState = !clone.isRunning
            repository.setRunningState(clone.id, newState)
        }
    }
}
