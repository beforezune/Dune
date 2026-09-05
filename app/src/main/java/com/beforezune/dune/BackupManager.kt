package com.beforezune.dune

import java.io.File

/** Coordinates a user-requested backup through the configured transport. */
class BackupManager(
    private val queue: BackupQueue,
    private val state: BackupQueueStore,
    private val transport: BackupTransport
) {
    fun enqueue(file: File): String = queue.enqueue(file)

    suspend fun upload(item: File): Result<String> {
        val jobId = item.nameWithoutExtension
        state.setStatus(jobId, BackupStatus.UPLOADING)
        val sourcePath = runCatching { item.readText() }.getOrNull()
            ?: return Result.failure(IllegalStateException("Invalid backup queue item"))
        val source = File(sourcePath)
        val result = transport.upload(source)
        if (result.isSuccess) state.setStatus(jobId, BackupStatus.UPLOADED)
        else state.setStatus(jobId, BackupStatus.FAILED)
        return result
    }
}
