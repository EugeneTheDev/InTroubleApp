package com.eugenethedev.introubleapp.presentation.alert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
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

    private val anim = AlphaAnimation(1.0f, 0.0f).also {
        it.duration = 500
        it.repeatCount = 1
        it.repeatMode = Animation.REVERSE
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

        alertButton.setOnLongClickListener {
            alertPresenter.onAlertButtonClick()
            true
        }

        alertPresenter.onCreate()
    }

    override fun setAfterAlertText() {
        Toast.makeText(requireContext(), R.string.alert_after_click_text, Toast.LENGTH_SHORT).show()
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