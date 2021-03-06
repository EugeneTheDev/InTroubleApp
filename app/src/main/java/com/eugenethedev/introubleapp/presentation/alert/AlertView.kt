package com.eugenethedev.introubleapp.presentation.alert

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


interface AlertView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setScreenText()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAfterAlertText()
}