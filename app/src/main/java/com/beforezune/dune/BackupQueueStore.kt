package com.beforezune.dune

import android.content.Context
import java.io.File

/** Simple persistent state for user-visible backup jobs. */
class BackupQueueStore(context: Context) {
    private val prefs = context.getSharedPreferences("backup_state", Context.MODE_PRIVATE)

    fun status(jobId: String): BackupStatus = runCatching {
        BackupStatus.valueOf(prefs.getString(jobId, BackupStatus.PENDING.name)!!)
    }.getOrDefault(BackupStatus.PENDING)

    fun setStatus(jobId: String, status: BackupStatus) {
        prefs.edit().putString(jobId, status.name).apply()
    }
}
