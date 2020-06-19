package com.eugenethedev.introubleapp.dagger

import android.content.Context
import com.eugenethedev.introubleapp.data.SettingsRepository
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun providesRealmConfig(): RealmConfiguration = RealmConfiguration.Builder().name("main").build()

    @Provides
    @Singleton
    fun providesLocationClient(context: Context) = LocationServices.getFusedLocationProviderClient(context)
}

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindIReceiverRepository(receiverRepository: SettingsRepository): ISettingsRepository
}