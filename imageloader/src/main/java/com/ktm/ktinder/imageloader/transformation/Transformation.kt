package com.ktm.ktinder.imageloader.transformation

import android.graphics.Bitmap

/**
 * User: Khang
 * Date: Tuesday, August 03 2021
 * Time: 11:08
 */
interface Transformation {

    /**
     * Return a unique key for this transformation. Use for caching.
     */
    val key: String

    /**
     * Apply the transformation to [originalBitmap] and return the transformed [Bitmap].
     *
     * @param originalBitmap The input [Bitmap] to transform.
     * @return The transformed [Bitmap].
     */
    suspend fun transform(originalBitmap: Bitmap): Bitmap
}