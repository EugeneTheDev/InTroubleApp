package com.eugenethedev.introubleapp.dagger

import android.content.Context
import com.eugenethedev.introubleapp.presentation.alert.AlertFragment
import com.eugenethedev.introubleapp.presentation.settings.SettingsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(alertFragment: AlertFragment)
    fun inject(settingsFragment: SettingsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}