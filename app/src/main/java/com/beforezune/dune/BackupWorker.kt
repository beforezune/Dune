package com.beforezune.dune

import android.content.Context
import java.io.File
import java.util.concurrent.Executors

/** Executes queued backups when explicitly requested by the app. */
class BackupWorker(context: Context) {
    private val queue = BackupQueue(context)
    private val state = BackupQueueStore(context)
    private val executor = Executors.newSingleThreadExecutor()

    fun submit(file: File, transport: BackupTransport, onComplete: (BackupStatus) -> Unit = {}) {
        val jobId = queue.enqueue(file)
        state.setStatus(jobId, BackupStatus.PENDING)
        executor.execute {
            state.setStatus(jobId, BackupStatus.UPLOADING)
            val result = try {
                kotlinx.coroutines.runBlocking { transport.upload(file) }
            } catch (_: Exception) {
                Result.failure<Unit>(IllegalStateException("Backup failed"))
            }
            val status = if (result.isSuccess) BackupStatus.UPLOADED else BackupStatus.FAILED
            state.setStatus(jobId, status)
            onComplete(status)
        }
    }
}
