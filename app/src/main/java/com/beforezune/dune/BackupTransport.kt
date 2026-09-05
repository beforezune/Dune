package com.beforezune.dune

import java.io.File

interface BackupTransport {
    suspend fun upload(file: File): Result<String>
}
