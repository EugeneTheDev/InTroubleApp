package com.eugenethedev.introubleapp.presentation.alert

import android.content.Context
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenethedev.introubleapp.InTroubleApp
import com.eugenethedev.introubleapp.R
import kotlinx.android.synthetic.main.fragment_alert.*
import javax.inject.Inject

class AlertFragment : MvpAppCompatFragment(), AlertView {

    @Inject
    @InjectPresenter
    lateinit var alertPresenter: AlertPresenter

    @ProvidePresenter
    fun provideAlertPresenter() = alertPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as InTroubleApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAlert_to_fragmentSettings)
        }

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
}