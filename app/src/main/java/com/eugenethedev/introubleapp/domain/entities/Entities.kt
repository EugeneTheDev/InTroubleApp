package com.eugenethedev.introubleapp.domain.entities

import io.realm.RealmList
import io.realm.RealmObject

open class Settings (
    var smsSetting: SmsSetting? = SmsSetting(),
    var cameraSettings: CameraSettings? = CameraSettings(),
    var foldersSettings: FoldersSettings? = FoldersSettings()
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

open class FoldersSettings(
    var isEnabled: Boolean = false,
    var folders: RealmList<Folder> = RealmList()
) : RealmObject()

open class Folder(
    var absolutePath: String = "",
    var displayPath: String = absolutePath.replace("/storage/emulated/0/", "./")
) : RealmObject()