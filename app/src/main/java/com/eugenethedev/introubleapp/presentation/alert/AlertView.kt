package com.eugenethedev.introubleapp.presentation.alert

import android.location.Location
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


interface AlertView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun sendMessages(numbers: List<String>, messageText: String, location: Location?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setScreenText()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAfterAlertText()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun startCamera()
}