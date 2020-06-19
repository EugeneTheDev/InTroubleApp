package com.eugenethedev.introubleapp.presentation.alert

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenethedev.introubleapp.InTroubleApp
import com.eugenethedev.introubleapp.R
import com.eugenethedev.introubleapp.presentation.isPermissionGranted
import kotlinx.android.synthetic.main.fragment_alert.*
import javax.inject.Inject

class AlertFragment : MvpAppCompatFragment(), AlertView {

    @Inject
    @InjectPresenter
    lateinit var alertPresenter: AlertPresenter

    @ProvidePresenter
    fun provideAlertPresenter() = alertPresenter

    private val anim = AlphaAnimation(1.0f, 0.0f).also {
        it.duration = 500
        it.repeatCount = 1
        it.repeatMode = Animation.REVERSE
    }

    companion object {
        private const val GOOGLE_MAP_LINK_TEMPLATE = "https://maps.google.com/?q=%s,%s"
    }

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
            alertPresenter.onAlertButtonClick()
        }

        alertPresenter.onCreate()
    }

    override fun sendMessages(numbers: List<String>, messageText: String, location: Location?) {
        if (isPermissionGranted(Manifest.permission.SEND_SMS)) {
            SmsManager.getDefault()?.let { manager ->
                val message = messageText + (location?.let { "\nLocation: ${GOOGLE_MAP_LINK_TEMPLATE.format(it.latitude, it.longitude)}" } ?: "")
                numbers.forEach {
                    manager.sendTextMessage(
                        it,
                        null,
                        message,
                        null,
                        null
                    )
                }
            }
        }
    }

    override fun setAfterAlertText() {
        alertText.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) { }
            override fun onAnimationStart(animation: Animation?) { }
            override fun onAnimationRepeat(animation: Animation?) {
                alertText.setText(R.string.alert_after_click_text)
            }
        })
    }

    override fun setScreenText() {
        alertText.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) { }
            override fun onAnimationStart(animation: Animation?) { }
            override fun onAnimationRepeat(animation: Animation?) {
                alertText.setText(R.string.alert_screen_text)
            }
        })
    }
}