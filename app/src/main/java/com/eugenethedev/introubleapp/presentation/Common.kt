package com.eugenethedev.introubleapp.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

typealias Permission = String

fun Fragment.isPermissionGranted(permission: Permission): Boolean {
    return requireContext().isPermissionGranted(permission)
}

fun Context.isPermissionGranted(permission: Permission): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}