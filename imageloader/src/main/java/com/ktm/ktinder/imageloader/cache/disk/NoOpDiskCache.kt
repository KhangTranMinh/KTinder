package com.ktm.ktinder.imageloader.cache.disk

import android.content.Context
import com.ktm.ktinder.imageloader.logger.Logger

class NoOpDiskCache(
    logger: Logger?,
    context: Context
) : DiskCache(logger, context) {

    override suspend fun isFileExisted(filePath: String) = false
}