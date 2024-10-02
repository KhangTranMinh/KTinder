package com.ktm.ktinder.imageloader.cache.mem

import android.graphics.Bitmap

abstract class MemCache {

    abstract fun getBitmap(key: String): Bitmap?

    abstract fun putBitmap(key: String, bitmap: Bitmap)

    abstract fun clearCache()
}