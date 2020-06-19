package com.eugenethedev.introubleapp.dagger

import com.eugenethedev.introubleapp.presentation.alert.AlertFragment
import com.eugenethedev.introubleapp.presentation.settings.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(alertFragment: AlertFragment)
    fun inject(settingsFragment: SettingsFragment)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}