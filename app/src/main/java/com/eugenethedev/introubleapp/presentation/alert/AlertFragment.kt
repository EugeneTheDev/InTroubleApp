package com.eugenethedev.introubleapp.presentation.alert

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eugenethedev.introubleapp.R
import kotlinx.android.synthetic.main.fragment_alert.*

class AlertFragment : Fragment() {

    companion object {
        private const val REQUEST_SMS_PERMISSION = 42
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
        } else {
            setupSms()
        }
    }

    private fun setupSms() {
        alertButton.setOnClickListener {
            SmsManager.getDefault()
                .sendTextMessage(
                    "+11111111", // some hardcoded number for now
                    null,
                    "Hello, world",
                    null,
                    null
                )

            Log.i("Message", "Message sent")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SMS_PERMISSION && resultCode == Activity.RESULT_OK) {
            setupSms()
        }
    }
}