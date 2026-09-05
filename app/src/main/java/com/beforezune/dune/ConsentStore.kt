package com.beforezune.dune

import android.content.Context

class ConsentStore(context: Context) {
    private val prefs = context.getSharedPreferences("dune_preferences", Context.MODE_PRIVATE)

    fun isConsentComplete(): Boolean = prefs.getBoolean(KEY_CONSENT, false)

    fun isAutomaticModeEnabled(): Boolean = prefs.getBoolean(KEY_AUTO_MODE, false)

    fun saveConsent(automaticMode: Boolean) {
        prefs.edit()
            .putBoolean(KEY_CONSENT, true)
            .putBoolean(KEY_AUTO_MODE, automaticMode)
            .apply()
    }

    fun setAutomaticModeEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_MODE, enabled).apply()
    }

    companion object {
        private const val KEY_CONSENT = "recording_consent"
        private const val KEY_AUTO_MODE = "automatic_recording_mode"
    }
}
