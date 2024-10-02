package com.ktm.ktinder.imageloader.cache.disk

import android.content.Context
import com.ktm.ktinder.imageloader.logger.Logger
import com.ktm.ktinder.imageloader.logger.logDebug
import com.ktm.ktinder.imageloader.util.withSeparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Arrays

abstract class DiskCache(private val logger: Logger?, context: Context) {

    companion object {
        const val CACHE_DIR_NAME = "ImageCache"
        const val MAX_SIZE = 100 * 1024 * 1024 // 100 MB = 100 * 1024 * 1024
        const val INVALIDATE_INTERVAL = 10 * 60 * 1000L // 10 minutes = 10 * 60 * 1000L
    }

    private val folder = File("${context.cacheDir.absolutePath}/$CACHE_DIR_NAME")

    private var invalidateTime = 0L

    init {
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw Exception("Can't create cache directory")
            }
        }
    }

    fun getCachedFile(fileName: String): File {
        return File("${folder.absolutePath}/$fileName")
    }

    abstract suspend fun isFileExisted(filePath: String): Boolean

    suspend fun invalidateCache() {
        withContext(Dispatchers.IO) {
            if (System.currentTimeMillis() - invalidateTime > INVALIDATE_INTERVAL) {
                logger?.logDebug(
                    "disk cache size: ${size().withSeparator()} | " +
                            "max size: ${MAX_SIZE.withSeparator()}"
                )
                invalidateTime = System.currentTimeMillis()
                if (size() > MAX_SIZE) trimCache()
            }
        }
    }

    suspend fun deleteCacheFile(file: File) {
        withContext(Dispatchers.IO) {
            if (file.exists()) file.delete()
        }
    }

    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            val files = folder.listFiles()
            files?.forEach {
                if (it.delete()) {
                    logger?.logDebug("delete file: ${it.name}")
                }
            }
        }
    }

    private fun trimCache() {
        logger?.logDebug("trim disk cache")
        val targetSize = (MAX_SIZE / 2).toLong()
        var currentCacheSize = size()
        if (currentCacheSize > targetSize) {
            val files = folder.listFiles()
            if (files != null) {
                // sort by last modified
                Arrays.sort(files) { lhs, rhs ->
                    when {
                        (lhs.lastModified() - rhs.lastModified()) > 0 -> 1
                        (lhs.lastModified() - rhs.lastModified()) < 0 -> -1
                        else -> 0
                    }
                }
                // delete files until the current size smaller than target size
                for (file: File in files) {
                    val size = file.length()
                    if (file.delete()) {
                        logger?.logDebug("delete file: ${file.name}")
                        currentCacheSize -= size
                        if (currentCacheSize <= targetSize) break
                    }
                }
            }
        }
    }

    private fun size(): Long {
        var result = 0L
        val files = folder.listFiles()
        files?.forEach { result += it.length() }
        return result
    }
}