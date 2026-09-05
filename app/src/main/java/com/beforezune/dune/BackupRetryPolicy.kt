package com.beforezune.dune

import kotlin.math.min

class BackupRetryPolicy {
    fun delayMillis(attempt: Int): Long {
        val safeAttempt = attempt.coerceAtLeast(0)
        return min(60_000L, 2_000L * (1L shl safeAttempt.coerceAtMost(5)))
    }
}
