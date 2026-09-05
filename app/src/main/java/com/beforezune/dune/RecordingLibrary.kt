package com.beforezune.dune

import android.content.Context
import android.media.MediaPlayer
import java.io.File

class RecordingLibrary(context: Context) {
    private val repository = RecordingRepository(context)
    private var player: MediaPlayer? = null

    fun recordings(): List<File> = repository.list()

    fun play(file: File, onComplete: (() -> Unit)? = null) {
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            setOnCompletionListener { onComplete?.invoke(); release(); player = null }
            prepare()
            start()
        }
    }

    fun stop() {
        player?.runCatching { stop() }
        player?.release()
        player = null
    }

    fun delete(file: File): Boolean = repository.delete(file)
}
