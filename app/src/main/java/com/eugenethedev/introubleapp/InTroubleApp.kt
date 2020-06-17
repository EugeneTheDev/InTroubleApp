package com.eugenethedev.introubleapp

import android.app.Application
import com.eugenethedev.introubleapp.dagger.AppComponent
import com.eugenethedev.introubleapp.dagger.DaggerAppComponent
import io.realm.Realm

class InTroubleApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        appComponent = DaggerAppComponent.builder().build()
    }
}