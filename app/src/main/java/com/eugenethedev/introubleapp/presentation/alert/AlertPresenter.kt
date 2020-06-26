package com.eugenethedev.introubleapp.presentation.alert

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.provider.MediaStore
import android.telephony.SmsManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenethedev.introubleapp.domain.entities.Settings
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import com.eugenethedev.introubleapp.presentation.isPermissionGranted
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

@InjectViewState
class AlertPresenter @Inject constructor(
    private val settingsRepository: ISettingsRepository,
    private val fusedClient: FusedLocationProviderClient,
    private val context: Context
) : MvpPresenter<AlertView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main
    private lateinit var settings: Settings

    companion object {
        private const val GOOGLE_MAP_LINK_TEMPLATE = "https://maps.google.com/?q=%s,%s"
    }

    fun onCreate() = launch {
        settings = settingsRepository.getSettings()
    }

    fun onAlertButtonClick() = launch {
        settings.smsSetting!!.takeIf { it.isEnabled }?.let { smsSetting ->
            val location = if (smsSetting.isLocationEnabled) {

                // this shitty code force location info to update
                fusedClient.requestLocationUpdates(LocationRequest.create().apply {
                    interval = 1
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    numUpdates = 1
                }, object : LocationCallback() {}, null)

                withContext(Dispatchers.IO) {
                    // in case location was just turned
                    if (!fusedClient.locationAvailability.await().isLocationAvailable) {
                        delay(3000)
                    }
                    fusedClient.lastLocation.await()
                }
            } else {
                null
            }

            sendMessages(
                numbers = smsSetting.receivers.map { it.number },
                messageText = smsSetting.messageText,
                location = location
            )
        }

        if (settings.cameraSettings!!.isEnabled) {
            launch {
                startCamera()
            }
        }

        settings.foldersSettings!!.takeIf { it.isEnabled }?.let { foldersSettings ->
            launch {
                deleteFolders(foldersSettings.folders.map { it.absolutePath })
            }
        }

        viewState.setAfterAlertText()
        delay(4000)
        viewState.setScreenText()
    }

    private fun sendMessages(numbers: List<String>, messageText: String, location: Location? = null) {
        if (context.isPermissionGranted(Manifest.permission.SEND_SMS)) {
            SmsManager.getDefault()?.let { manager ->
                val message = messageText + (location?.let { "\nLocation: ${GOOGLE_MAP_LINK_TEMPLATE.format(it.latitude, it.longitude)}" } ?: "")
                numbers.forEach {
                    manager.sendTextMessage(
                        it,
                        null,
                        message,
                        null,
                        null
                    )
                }
            }
        }
    }

    private fun deleteFolders(foldersPaths: List<String>) {
        foldersPaths.map { File(it) }
            .filter { it.isDirectory && it.exists() }
            .map { it.deleteRecursively() }
    }

    private fun startCamera() {
        context.startActivity(Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun <T> Task<T>.await(): T = Tasks.await(this)
}