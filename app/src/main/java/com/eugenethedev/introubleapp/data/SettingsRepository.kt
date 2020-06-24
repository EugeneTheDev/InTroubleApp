package com.eugenethedev.introubleapp.data

import com.eugenethedev.introubleapp.domain.entities.Receiver
import com.eugenethedev.introubleapp.domain.entities.Settings
import com.eugenethedev.introubleapp.domain.repository.ISettingsRepository
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import java.util.NoSuchElementException
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val realmConfig: RealmConfiguration
) : ISettingsRepository {
    private lateinit var realm: Realm
    private lateinit var settings: Settings

    @Throws(NoSuchElementException::class)
    override suspend fun getSettings(): Settings {
        realm = Realm.getInstance(realmConfig) // create new realm instance when retrieving settings
        if (realm.where<Settings>().count() == 0L) {
            realm.executeTransaction {
                it.insert(Settings())
            }
        }
        settings = realm.where<Settings>().findFirst()!!
        return settings
    }

    override suspend fun flush() {
        realm.close() // close realm instance after all changes
    }

    override suspend fun toggleSms(isEnabled: Boolean) {
        realm.executeTransaction {
            settings.smsSetting!!.isEnabled = isEnabled
        }
    }

    override suspend fun addReceiver(name: String, number: String) {
        realm.executeTransaction {
            val receiver = it.copyToRealm(Receiver(name, number))
            settings.smsSetting!!.receivers.add(receiver)
        }
    }

    override suspend fun removeReceiver(receiver: Receiver) {
        realm.executeTransaction {
            settings.smsSetting!!.receivers.remove(receiver)
        }
    }

    override suspend fun changeMessageText(messageText: String) {
        realm.executeTransaction {
            settings.smsSetting!!.messageText = messageText
        }
    }

    override suspend fun toggleLocation(isEnabled: Boolean) {
        realm.executeTransaction {
            settings.smsSetting!!.isLocationEnabled = isEnabled
        }
    }

    override suspend fun toggleCamera(isEnabled: Boolean) {
        realm.executeTransaction {
            settings.cameraSettings!!.isEnabled = isEnabled
        }
    }

}