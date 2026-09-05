package com.beforezune.dune

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var consentStore: ConsentStore
    private lateinit var library: RecordingLibrary
    private lateinit var listText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consentStore = ConsentStore(this)
        library = RecordingLibrary(this)
        showScreen()
    }

    private fun showScreen() {
        val title = TextView(this).apply { text = "Dune\n\nParental safety"; textSize = 24f }
        val status = TextView(this).apply { textSize = 16f }
        val automatic = Switch(this).apply {
            text = "Automatic recording mode"
            isChecked = consentStore.isAutomaticModeEnabled()
            isEnabled = consentStore.isConsentComplete()
            setOnCheckedChangeListener { _, checked -> consentStore.setAutomaticModeEnabled(checked); refreshStatus(status) }
        }
        val refresh = Button(this).apply { text = "Refresh recordings"; setOnClickListener { refreshList() } }
        val stop = Button(this).apply { text = "Stop playback"; setOnClickListener { library.stop() } }
        listText = TextView(this).apply { textSize = 16f }
        refreshStatus(status)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 80, 48, 48)
            addView(title); addView(status); addView(automatic); addView(refresh); addView(stop); addView(listText)
        }
        setContentView(layout)
        refreshList()
    }

    private fun refreshStatus(view: TextView) {
        view.text = if (consentStore.isConsentComplete()) "Recording consent: enabled\nAutomatic mode: ${if (consentStore.isAutomaticModeEnabled()) "on" else "off"}" else "Recording consent has not been configured."
    }

    private fun refreshList() {
        val files = library.recordings()
        listText.text = if (files.isEmpty()) "\nNo recordings yet." else "\nRecordings:\n" + files.joinToString("\n") { "• ${it.name}" }
    }
}
