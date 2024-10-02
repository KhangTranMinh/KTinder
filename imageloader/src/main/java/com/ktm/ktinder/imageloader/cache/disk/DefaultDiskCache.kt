package com.ktm.ktinder.imageloader.cache.disk

import android.content.Context
import com.ktm.ktinder.imageloader.logger.Logger
import com.ktm.ktinder.imageloader.logger.logDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DefaultDiskCache(
    private val logger: Logger?,
    context: Context
) : DiskCache(logger, context) {

    override suspend fun isFileExisted(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            logger?.logDebug("check existed file")
            kotlin.runCatching {
                File(filePath).exists()
            }.onFailure {
                logger?.logDebug("check existed file FAIL", it)
            }.getOrDefault(false)
        }
    }
}