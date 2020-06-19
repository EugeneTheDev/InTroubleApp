package com.eugenethedev.introubleapp.presentation.alert

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleTagStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


interface AlertView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun sendMessages(numbers: List<String>, messageText: String)

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "text")
    fun setScreenText()

    @StateStrategyType(AddToEndSingleTagStrategy::class, tag = "text")
    fun setAfterAlertText()
}