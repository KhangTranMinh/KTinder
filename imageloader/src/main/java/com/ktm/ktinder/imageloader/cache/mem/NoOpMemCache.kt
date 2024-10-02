package com.ktm.ktinder.imageloader.cache.mem

import android.graphics.Bitmap

class NoOpMemCache : MemCache() {

    override fun getBitmap(key: String): Bitmap? = null

    override fun putBitmap(key: String, bitmap: Bitmap) {
    }

    override fun clearCache() {
    }
}