package com.beforezune.dune

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import java.io.File

class RecordingService : Service() {
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startRecording()
            ACTION_STOP -> stopRecording()
        }
        return START_NOT_STICKY
    }

    private fun startRecording() {
        if (recorder != null) return
        createChannel()
        startForeground(NOTIFICATION_ID, notification())

        val dir = File(filesDir, "recordings").apply { mkdirs() }
        outputFile = File(dir, "recording_${System.currentTimeMillis()}.m4a")

        recorder = MediaRecorder(this).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile!!.absolutePath)
            prepare()
            start()
        }
    }

    private fun stopRecording() {
        recorder?.runCatching { stop() }
        recorder?.release()
        recorder = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Dune recording",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun notification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Dune recording")
                .setContentText("Audio recording is active")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setOngoing(true)
                .build()
        } else {
            Notification.Builder(this)
                .setContentTitle("Dune recording")
                .setContentText("Audio recording is active")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setOngoing(true)
                .build()
        }
    }

    override fun onDestroy() {
        recorder?.runCatching { stop() }
        recorder?.release()
        recorder = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "com.beforezune.dune.START_RECORDING"
        const val ACTION_STOP = "com.beforezune.dune.STOP_RECORDING"
        private const val CHANNEL_ID = "dune_recording"
        private const val NOTIFICATION_ID = 1001
    }
}
