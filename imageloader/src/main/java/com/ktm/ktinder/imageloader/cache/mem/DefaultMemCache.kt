package com.ktm.ktinder.imageloader.cache.mem

import android.graphics.Bitmap
import android.util.LruCache
import com.ktm.ktinder.imageloader.extension.sizeInKB
import com.ktm.ktinder.imageloader.logger.Logger
import com.ktm.ktinder.imageloader.logger.logDebug
import com.ktm.ktinder.imageloader.util.withSeparator

class DefaultMemCache(private val logger: Logger?) : MemCache() {

    // calculated in KB
    private val maxSize = ((Runtime.getRuntime().maxMemory() / 1024).toInt()) / 8

    private val lruCache = object : LruCache<String, Bitmap>(maxSize) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int {
            return bitmap.sizeInKB()
        }

        override fun entryRemoved(
            evicted: Boolean,
            key: String,
            oldValue: Bitmap?,
            newValue: Bitmap?
        ) {
            super.entryRemoved(evicted, key, oldValue, newValue)
            logger?.logDebug(
                "remove bitmap with key: $key | size: ${oldValue?.sizeInKB()?.withSeparator()}"
            )
            Runtime.getRuntime().maxMemory()
        }
    }

    override fun getBitmap(key: String): Bitmap? = lruCache.get(key)

    override fun putBitmap(key: String, bitmap: Bitmap) {
        lruCache.put(key, bitmap)
        logger?.logDebug(
            "mem cache size: ${lruCache.size().withSeparator()} | " +
                    "max size: ${lruCache.maxSize().withSeparator()}"
        )
    }

    override fun clearCache() = lruCache.evictAll()
}