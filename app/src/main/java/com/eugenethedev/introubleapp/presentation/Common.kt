package com.eugenethedev.introubleapp.presentation

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

typealias Permission = String

fun Fragment.isPermissionGranted(permission: Permission): Boolean {
    return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}