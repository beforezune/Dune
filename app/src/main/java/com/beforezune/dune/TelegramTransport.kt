package com.beforezune.dune

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** User-configured Telegram Bot API transport. */
class TelegramTransport(private val config: TelegramConfigStore) : BackupTransport {
    override fun upload(file: File): Result<String> {
        val token = config.botToken() ?: return Result.failure(IllegalStateException("Telegram backup is not configured"))
        val chatId = config.chatId() ?: return Result.failure(IllegalStateException("Telegram backup is not configured"))
        if (!file.isFile) return Result.failure(IllegalArgumentException("Backup file does not exist"))
        return try {
            val boundary = "----Dune${System.currentTimeMillis()}"
            val connection = (URL("https://api.telegram.org/bot$token/sendDocument").openConnection() as HttpURLConnection)
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            connection.outputStream.use { out ->
                out.write("--$boundary\r\nContent-Disposition: form-data; name=\"chat_id\"\r\n\r\n$chatId\r\n".toByteArray())
                out.write("--$boundary\r\nContent-Disposition: form-data; name=\"document\"; filename=\"${file.name}\"\r\nContent-Type: application/octet-stream\r\n\r\n".toByteArray())
                file.inputStream().use { it.copyTo(out) }
                out.write("\r\n--$boundary--\r\n".toByteArray())
            }
            val code = connection.responseCode
            connection.disconnect()
            if (code !in 200..299) Result.failure(IllegalStateException("Telegram upload failed: HTTP $code"))
            else Result.success("uploaded:${file.name}")
        } catch (error: Exception) { Result.failure(error) }
    }
}
