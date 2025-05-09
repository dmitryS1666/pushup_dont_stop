package com.pushup.donstop.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.pushup.donstop.R

class SettingsFragment : Fragment() {

    private lateinit var switchSound: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchSound = view.findViewById(R.id.switchSound)
        val infoSection: View = view.findViewById(R.id.btnInfo)
        val privacySection: View = view.findViewById(R.id.btnPrivacy)

        val prefs = requireContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val soundEnabled = prefs.getBoolean("sound_enabled", true)
        switchSound.isChecked = soundEnabled

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sound_enabled", isChecked).apply()
        }

        infoSection.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, InfoFragment())
                .addToBackStack(null)
                .commit()
        }

        privacySection.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, PrivacyFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
