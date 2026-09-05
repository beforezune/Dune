package com.beforezune.dune

import android.content.Context

/** Stores user-provided Telegram backup settings locally. Never commits secrets to source control. */
class TelegramConfigStore(context: Context) {
    private val prefs = context.getSharedPreferences("telegram_backup", Context.MODE_PRIVATE)

    fun isConfigured(): Boolean =
        !prefs.getString(KEY_BOT_TOKEN, null).isNullOrBlank() &&
        !prefs.getString(KEY_CHAT_ID, null).isNullOrBlank()

    fun save(botToken: String, chatId: String) {
        require(botToken.isNotBlank()) { "Bot token is required" }
        require(chatId.isNotBlank()) { "Chat ID is required" }
        prefs.edit()
            .putString(KEY_BOT_TOKEN, botToken.trim())
            .putString(KEY_CHAT_ID, chatId.trim())
            .apply()
    }

    fun botToken(): String? = prefs.getString(KEY_BOT_TOKEN, null)
    fun chatId(): String? = prefs.getString(KEY_CHAT_ID, null)

    fun clear() = prefs.edit().clear().apply()

    companion object {
        private const val KEY_BOT_TOKEN = "bot_token"
        private const val KEY_CHAT_ID = "chat_id"
    }
}
