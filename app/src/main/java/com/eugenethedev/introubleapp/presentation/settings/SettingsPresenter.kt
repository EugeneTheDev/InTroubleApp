package com.eugenethedev.introubleapp.presentation.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenethedev.introubleapp.domain.entities.Receiver
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
    private val settingsRepository: ISettingsRepository
) : MvpPresenter<SettingsView>(), CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    fun onCreate() = launch {
        val settings = settingsRepository.getSettings()
        settings.smsSetting!!.let {
            viewState.setSmsToggleState(it.isEnabled)
            viewState.setupReceiversList(it.receivers)
            viewState.setMessageText(it.messageText)
            viewState.setLocationToggleState(it.isLocationEnabled)
        }

        viewState.setCameraToggleState(settings.cameraSettings!!.isEnabled)

        viewState.makeVisible()
    }

    fun onToggleSms(isChecked: Boolean) = launch {
        settingsRepository.toggleSms(isChecked)
    }

    fun onAddReceiver(name: String, number: String) = launch {
        settingsRepository.addReceiver(name, number)
    }

    fun onRemoveReceiver(receiver: Receiver) = launch {
        settingsRepository.removeReceiver(receiver)
    }

    fun onMessageTextChanged(messageText: String) = launch {
        settingsRepository.changeMessageText(messageText)
    }

    fun onToggleLocation(isChecked: Boolean) = launch {
        settingsRepository.toggleLocation(isChecked)
    }

    fun onToggleCamera(isChecked: Boolean) = launch {
        settingsRepository.toggleCamera(isChecked)
    }

    override fun onDestroy() {
        launch {
            settingsRepository.flush()
        }
    }

}