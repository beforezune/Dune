package com.beforezune.dune

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class TelegramSettings : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = TelegramConfigStore(this)
        val token = EditText(this).apply { hint = "Telegram bot token"; setText(store.botToken() ?: "") }
        val chat = EditText(this).apply { hint = "Telegram chat ID"; setText(store.chatId() ?: "") }
        val status = TextView(this)
        val save = Button(this).apply {
            text = "Save Telegram settings"
            setOnClickListener {
                runCatching { store.save(token.text.toString().trim(), chat.text.toString().trim()) }
                    .onSuccess { status.text = "Telegram configuration saved." }
                    .onFailure { status.text = it.message ?: "Enter both values." }
            }
        }
        val clear = Button(this).apply {
            text = "Clear Telegram settings"
            setOnClickListener { store.clear(); token.setText(""); chat.setText(""); status.text = "Configuration cleared." }
        }
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 80, 48, 48)
            addView(TextView(this@TelegramSettings).apply { text = "Telegram backup"; textSize = 24f })
            addView(TextView(this@TelegramSettings).apply { text = "Only configure a bot and destination you are authorized to use." })
            addView(token); addView(chat); addView(save); addView(clear); addView(status)
        })
    }
}
