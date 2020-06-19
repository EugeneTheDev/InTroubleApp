package com.eugenethedev.introubleapp.dagger

import com.eugenethedev.introubleapp.data.SettingsRepository
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
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
}

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindIReceiverRepository(receiverRepository: SettingsRepository): ISettingsRepository
}