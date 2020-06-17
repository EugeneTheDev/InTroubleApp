package com.eugenethedev.introubleapp.dagger

import dagger.Module
import dagger.Provides
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun providesRealmConfig(): RealmConfiguration = RealmConfiguration.Builder().name("main").build()
}