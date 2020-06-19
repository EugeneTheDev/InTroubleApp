package com.eugenethedev.introubleapp.presentation.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eugenethedev.introubleapp.R
import com.eugenethedev.introubleapp.presentation.isPermissionGranted
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    companion object {
        private const val REQUEST_SMS_PERMISSION = 42
        private const val REQUEST_CONTACTS_PERMISSION = 43
        private const val REQUEST_PICK_CONTACT = 44
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)?.let {
            it.setSupportActionBar(settingsToolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        setHasOptionsMenu(true)

        val adapter = ReceiversAdapter()
        receiversList.adapter = adapter
        addReceiverButton.setOnClickListener {
            adapter.addNewAddItem()
        }

        smsToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isPermissionGranted(Manifest.permission.SEND_SMS)) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
            } else {
                smsToggleChange(isChecked)
            }
        }

        addReceiverButton.setOnClickListener {
            if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS_PERMISSION )
            } else {
                addNewReceiver()
            }
        }
    }

    private fun smsToggleChange(isChecked: Boolean) {

    }

    private fun addNewReceiver() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI).also {
                it.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            },
            REQUEST_PICK_CONTACT
        )
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        android.R.id.home -> findNavController().popBackStack()
        else -> false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_SMS_PERMISSION -> {
                if (isPermissionGranted(Manifest.permission.SEND_SMS)) {
                    smsToggleChange(smsToggle.isChecked)
                } else {
                    smsToggle.isChecked = false
                }
            }

            REQUEST_CONTACTS_PERMISSION -> {
                if (isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                    addNewReceiver()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            intent?.data?.let { uri ->
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
                requireActivity().contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                    cursor.moveToFirst()
                    val nameColumnsIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val name = cursor.getString(nameColumnsIndex)
                    val numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val number = cursor.getString(numberColumnIndex)
                }
            }
        }
    }

}