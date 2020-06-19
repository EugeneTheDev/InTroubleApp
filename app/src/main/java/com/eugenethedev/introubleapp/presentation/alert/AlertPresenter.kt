package com.eugenethedev.introubleapp.presentation.alert

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenethedev.introubleapp.domain.entities.Settings
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import com.google.android.gms.location.FusedLocationProviderClient
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
        settings.smsSetting?.let { smsSetting ->
            if (smsSetting.isEnabled) {
                val isLocationEnabled = smsSetting.isLocationEnabled
                val location = withContext(Dispatchers.IO) {
                    if (isLocationEnabled) {
                        Tasks.await(fusedClient.lastLocation)
                    } else {
                        null
                    }
                }

                viewState.sendMessages(
                    numbers = smsSetting.receivers.map { it.number },
                    messageText = smsSetting.messageText,
                    location = location
                )
            }
        }

        viewState.setAfterAlertText()
        delay(4000)
        viewState.setScreenText()
    }
}