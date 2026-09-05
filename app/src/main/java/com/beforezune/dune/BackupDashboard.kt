package com.beforezune.dune

import java.io.File

class BackupDashboard(private val queue: BackupQueue, private val states: BackupQueueStore) {
    fun queueForBackup(file: File): String = queue.enqueue(file).also {
        states.setStatus(it, BackupStatus.PENDING)
    }

    fun status(jobId: String): BackupStatus = states.status(jobId)
}
