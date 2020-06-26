package com.eugenethedev.introubleapp.presentation.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenethedev.introubleapp.InTroubleApp
import com.eugenethedev.introubleapp.R
import com.eugenethedev.introubleapp.domain.entities.Folder
import com.eugenethedev.introubleapp.domain.entities.Receiver
import com.eugenethedev.introubleapp.presentation.isPermissionGranted
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_settings.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

class SettingsFragment : MvpAppCompatFragment(), SettingsView {

    companion object {
        private const val REQUEST_SMS_PERMISSION = 42
        private const val REQUEST_CONTACTS_PERMISSION = 43
        private const val REQUEST_PICK_CONTACT = 44
        private const val REQUEST_LOCATION_PERMISSION = 45
        private const val REQUEST_WRITE_STORAGE_PERMISSION = 46
        private const val REQUEST_PICK_FOLDER = 47
    }

    @Inject
    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    @ProvidePresenter
    fun provideSettingsPresenter() = settingsPresenter

    private lateinit var receiversAdapter: ReceiversAdapter
    private lateinit var foldersAdapter: FoldersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as InTroubleApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)?.let {
            it.setSupportActionBar(settingsToolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        setHasOptionsMenu(true)

        smsToggle.setOnCheckedChangeListener { _, isChecked ->
            enableDisableBlock(smsBlock, isChecked)
            if (isChecked && !isPermissionGranted(Manifest.permission.SEND_SMS)) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
            } else {
                settingsPresenter.onToggleSms(isChecked)
            }
        }

        addReceiverButton.setOnClickListener {
            if (!isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS_PERMISSION)
            } else {
                addNewReceiver()
            }
        }

        messageEditText.setOnFocusChangeListener { _, _ ->
            settingsPresenter.onMessageTextChanged(messageEditText.text.toString())
        }

        locationToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            } else {
                settingsPresenter.onToggleLocation(isChecked)
            }
        }

        foldersToggle.setOnCheckedChangeListener {  _, isChecked ->
            enableDisableBlock(foldersBlock, isChecked)
            if (isChecked && !isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_STORAGE_PERMISSION)
            } else {
                settingsPresenter.onToggleFolders(isChecked)
            }
        }

        cameraToggle.setOnCheckedChangeListener { _, isChecked ->
            enableDisableBlock(cameraBlock, isChecked)
            settingsPresenter.onToggleCamera(isChecked)
        }

        addFolderButton.setOnClickListener {
            val chooseIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).also {
                it.addCategory(Intent.CATEGORY_DEFAULT)
            }
            startActivityForResult(Intent.createChooser(chooseIntent, getString(R.string.pick_folder_text)), REQUEST_PICK_FOLDER)
        }

        settingsPresenter.onCreate()
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
                    settingsPresenter.onToggleSms(smsToggle.isChecked)
                } else {
                    smsToggle.isChecked = false
                }
            }

            REQUEST_CONTACTS_PERMISSION -> {
                if (isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                    addNewReceiver()
                }
            }

            REQUEST_LOCATION_PERMISSION -> {
                if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    settingsPresenter.onToggleLocation(locationToggle.isChecked)
                } else {
                    locationToggle.isChecked = false
                }
            }

            REQUEST_WRITE_STORAGE_PERMISSION -> {
                if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    settingsPresenter.onToggleFolders(foldersToggle.isChecked)
                } else {
                    foldersToggle.isChecked = false
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_CONTACT -> intent?.data?.let { uri ->
                    val projection = arrayOf(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                    requireActivity().contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                            cursor.moveToFirst()
                            val nameColumnsIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                            val name = cursor.getString(nameColumnsIndex)
                            val numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val number = cursor.getString(numberColumnIndex)
                            settingsPresenter.onAddReceiver(name, number)
                    }
                }

                REQUEST_PICK_FOLDER -> intent?.data?.let { uri ->
                    val path = uri.path ?: throw IllegalArgumentException("Null path")

                    if ("/tree/primary:" in path) {
                        settingsPresenter.onAddFolder(path.replace("/tree/primary:", "/storage/emulated/0/"))
                    } else {
                        Toast.makeText(requireContext(), R.string.incorrect_folder, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun setSmsToggleState(isChecked: Boolean) {
        smsToggle.isChecked = isChecked
        smsToggle.jumpDrawablesToCurrentState()
        enableDisableBlock(smsBlock, isChecked)
    }

    override fun setLocationToggleState(isChecked: Boolean) {
        locationToggle.isChecked = isChecked
        locationToggle.jumpDrawablesToCurrentState()
    }

    override fun setupReceiversList(receivers: RealmList<Receiver>) {
        receiversAdapter = ReceiversAdapter(
            receivers = receivers,
            removeReceiver = { settingsPresenter.onRemoveReceiver(it) }
        )
        receiversList.adapter = receiversAdapter
    }

    override fun setupFoldersList(folders: RealmList<Folder>) {
        foldersAdapter = FoldersAdapter(
            folders = folders,
            removeFolder = { settingsPresenter.onRemoveFolder(it) }
        )
        foldersList.adapter = foldersAdapter
    }

    override fun setFoldersToggleState(isChecked: Boolean) {
        foldersToggle.isChecked = isChecked
        foldersToggle.jumpDrawablesToCurrentState()
        enableDisableBlock(foldersBlock, isChecked)
    }

    override fun setMessageText(messageText: String) {
        messageEditText.text = SpannableStringBuilder(messageText)
    }

    override fun setCameraToggleState(isChecked: Boolean) {
        cameraToggle.isChecked = isChecked
        cameraToggle.jumpDrawablesToCurrentState()
        enableDisableBlock(cameraBlock, isChecked)
    }

    override fun makeVisible() {
        settingsNestedScrollView.visibility = View.VISIBLE
    }

    private fun enableDisableBlock(block: ViewGroup, isEnabled: Boolean) {
        block.alpha = if (isEnabled) 1f else 0.5f
        enableDisableRecursively(block, isEnabled)
    }

    private fun enableDisableRecursively(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
        view.takeIf { it is ViewGroup }
            ?.let { it as ViewGroup }
            ?.let {
                it.children.forEach { child ->
                    enableDisableRecursively(child, isEnabled)
                }
            }
    }

}