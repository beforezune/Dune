package com.beforezune.dune

import java.io.File

interface BackupTransport {
    fun upload(file: File): Result<String>
}
