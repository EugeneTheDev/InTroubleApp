package com.eugenethedev.introubleapp.domain.entities

import io.realm.RealmList
import io.realm.RealmObject

open class Settings (
    var smsSetting: SmsSetting? = SmsSetting(),
    var cameraSettings: CameraSettings? = CameraSettings()
) : RealmObject()

open class SmsSetting (
    var isEnabled: Boolean = false,
    var receivers: RealmList<Receiver> = RealmList(),
    var messageText: String = "Sent from InTroubleApp",
    var isLocationEnabled: Boolean = false
) : RealmObject()

open class Receiver (
    var name: String = "",
    var number: String = ""
) : RealmObject()

open class CameraSettings(
    var isEnabled: Boolean = false
) : RealmObject()