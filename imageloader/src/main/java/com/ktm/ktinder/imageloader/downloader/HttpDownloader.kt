package com.ktm.ktinder.imageloader.downloader

import com.ktm.ktinder.imageloader.logger.Logger
import com.ktm.ktinder.imageloader.logger.logDebug
import com.ktm.ktinder.imageloader.util.withSeparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class HttpDownloader(private val logger: Logger?) {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun download(url: String, file: File): Boolean {
        return withContext(Dispatchers.IO) {
            ensureActive()
            kotlin.runCatching {
                logger?.logDebug("download \n $url")
                val request = Request.Builder().url(url).build()
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    ensureActive()
                    val fileContentAsBytes = response.body?.bytes()
                    fileContentAsBytes?.let { bytes ->
                        FileOutputStream(file).use { stream ->
                            ensureActive()
                            stream.write(bytes)
                        }
                        logger?.logDebug("download completed with size: ${(bytes.size).withSeparator()}")
                    }
                }
                response.body?.close()
                true
            }.onFailure {
                logger?.logDebug("download FAIL", it)
            }.getOrDefault(false)
        }
    }
}