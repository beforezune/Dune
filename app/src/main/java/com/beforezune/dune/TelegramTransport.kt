package com.beforezune.dune

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** User-configured Telegram Bot API transport. */
class TelegramTransport(private val config: TelegramConfigStore) : BackupTransport {
    override suspend fun upload(file: File): Result<String> = withContext(Dispatchers.IO) {
        val values = config.load() ?: return@withContext Result.failure(IllegalStateException("Telegram backup is not configured"))
        runCatching {
            val boundary = "----Dune${System.currentTimeMillis()}"
            val connection = (URL("https://api.telegram.org/bot${values.first}/sendDocument").openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = 15_000
                readTimeout = 30_000
                setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            }
            connection.outputStream.use { out ->
                fun part(name: String, value: String) {
                    out.write("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"\r\n\r\n$value\r\n".toByteArray())
                }
                part("chat_id", values.second)
                out.write("--$boundary\r\nContent-Disposition: form-data; name=\"document\"; filename=\"${file.name}\"\r\nContent-Type: application/octet-stream\r\n\r\n".toByteArray())
                file.inputStream().use { it.copyTo(out) }
                out.write("\r\n--$boundary--\r\n".toByteArray())
            }
            val code = connection.responseCode
            if (code !in 200..299) error("Telegram upload failed: HTTP $code")
            connection.disconnect()
            "uploaded:${file.name}"
        }
    }
}
