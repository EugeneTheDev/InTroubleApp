package com.eugenethedev.introubleapp.presentation.alert

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenethedev.introubleapp.domain.entities.Settings
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class AlertPresenter @Inject constructor(
    private val settingsRepository: ISettingsRepository
) : MvpPresenter<AlertView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main
    private lateinit var settings: Settings

    fun onCreate() = launch {
        settings = settingsRepository.getSettings()
    }

    fun onAlertButtonClick() = launch {
        settings.smsSetting?.let { smsSetting ->
            if (smsSetting.isEnabled) {
                viewState.sendMessages(
                    smsSetting.receivers.map { it.number },
                    smsSetting.messageText
                )
            }
        }

        viewState.setAfterAlertText()
        delay(4000)
        viewState.setScreenText()
    }
}