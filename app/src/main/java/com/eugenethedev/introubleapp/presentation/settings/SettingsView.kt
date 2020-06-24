package com.eugenethedev.introubleapp.presentation.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.eugenethedev.introubleapp.domain.entities.Receiver
import io.realm.RealmList

interface SettingsView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSmsToggleState(isChecked: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setLocationToggleState(isChecked: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setMessageText(messageText: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupReceiversList(receivers: RealmList<Receiver>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setCameraToggleState(isChecked: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun makeVisible()
}