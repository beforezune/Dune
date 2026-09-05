package com.beforezune.dune

import android.content.Context
import java.io.File
import java.util.UUID

/** Durable, user-visible backup queue. Network transport is intentionally separate. */
class BackupQueue(context: Context) {
    private val queueDir = File(context.filesDir, "backup_queue").apply { mkdirs() }

    fun enqueue(file: File): String {
        require(file.exists()) { "Recording does not exist" }
        val id = UUID.randomUUID().toString()
        File(queueDir, "$id.queue").writeText(file.absolutePath)
        return id
    }

    fun pending(): List<File> = queueDir.listFiles()
        ?.filter { it.extension == "queue" }
        ?.sortedBy { it.lastModified() }
        ?: emptyList()

    fun remove(item: File): Boolean = item.delete()
}
