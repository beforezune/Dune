package com.beforezune.dune

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var consentStore: ConsentStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consentStore = ConsentStore(this)
        showScreen()
    }

    private fun showScreen() {
        val title = TextView(this).apply {
            text = "Dune\n\nParental safety"
            textSize = 24f
        }
        val status = TextView(this).apply {
            textSize = 16f
            text = if (consentStore.isConsentComplete()) {
                "Recording consent: enabled\nAutomatic mode: ${if (consentStore.isAutomaticModeEnabled()) "on" else "off"}"
            } else {
                "Recording consent has not been configured."
            }
        }
        val automatic = Switch(this).apply {
            text = "Automatic recording mode"
            isChecked = consentStore.isAutomaticModeEnabled()
            isEnabled = consentStore.isConsentComplete()
            setOnCheckedChangeListener { _, checked ->
                consentStore.setAutomaticModeEnabled(checked)
                status.text = "Recording consent: enabled\nAutomatic mode: ${if (checked) "on" else "off"}"
            }
        }
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 80, 48, 48)
            addView(title)
            addView(status)
            addView(automatic)
        }
        setContentView(layout)
    }
}
