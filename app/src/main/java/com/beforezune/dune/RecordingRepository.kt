package com.beforezune.dune

import android.content.Context
import java.io.File

/** Local recording index. Files remain in app-private storage. */
class RecordingRepository(context: Context) {
    private val directory = File(context.filesDir, "recordings").apply { mkdirs() }

    fun list(): List<File> = directory.listFiles()
        ?.filter { it.isFile }
        ?.sortedByDescending { it.lastModified() }
        ?: emptyList()

    fun delete(file: File): Boolean = file.delete()
}
