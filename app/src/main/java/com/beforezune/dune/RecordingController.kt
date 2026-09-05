package com.beforezune.dune

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class RecordingController(private val context: Context) {
    fun hasMicrophonePermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    fun start(): Result<Unit> {
        if (!hasMicrophonePermission()) return Result.failure(SecurityException("Microphone permission is required"))
        val intent = Intent(context, RecordingService::class.java).setAction(RecordingService.ACTION_START)
        ContextCompat.startForegroundService(context, intent)
        return Result.success(Unit)
    }

    fun stop() {
        val intent = Intent(context, RecordingService::class.java).setAction(RecordingService.ACTION_STOP)
        context.startService(intent)
    }
}
