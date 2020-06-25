package com.eugenethedev.introubleapp.presentation.alert

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenethedev.introubleapp.domain.entities.Settings
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class AlertPresenter @Inject constructor(
    private val settingsRepository: ISettingsRepository,
    private val fusedClient: FusedLocationProviderClient
) : MvpPresenter<AlertView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main
    private lateinit var settings: Settings

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
                    Tasks.await(fusedClient.lastLocation)
                }
            } else {
                null
            }

            viewState.sendMessages(
                numbers = smsSetting.receivers.map { it.number },
                messageText = smsSetting.messageText,
                location = location
            )

        }

        if (settings.cameraSettings!!.isEnabled) {
            viewState.startCamera()
        }

        settings.foldersSettings!!.takeIf { it.isEnabled }?.let { foldersSettings ->
            viewState.deleteFolders(foldersSettings.folders.map { it.absolutePath })
        }

        viewState.setAfterAlertText()
        delay(4000)
        viewState.setScreenText()
    }
}