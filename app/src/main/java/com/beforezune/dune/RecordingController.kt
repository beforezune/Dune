package com.beforezune.dune

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

class RecordingController(private val context: Context) {
    fun hasMicrophonePermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    fun start(): Result<Unit> {
        if (!hasMicrophonePermission()) return Result.failure(SecurityException("Microphone permission is required"))
        val intent = Intent(context, RecordingService::class.java).setAction(RecordingService.ACTION_START)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent) else context.startService(intent)
        return Result.success(Unit)
    }

    fun stop() {
        context.startService(Intent(context, RecordingService::class.java).setAction(RecordingService.ACTION_STOP))
    }
}
