package com.eugenethedev.introubleapp.domain.repository

import com.eugenethedev.introubleapp.domain.entities.Receiver
import com.eugenethedev.introubleapp.domain.entities.Settings

interface ISettingsRepository {
    suspend fun getSettings(): Settings
    suspend fun toggleSms(isEnabled: Boolean)
    suspend fun addReceiver(name: String, number: String)
    suspend fun removeReceiver(receiver: Receiver)
    suspend fun changeMessageText(messageText: String)
    suspend fun toggleLocation(isEnabled: Boolean)
    suspend fun toggleCamera(isEnabled: Boolean)
    suspend fun flush()
}