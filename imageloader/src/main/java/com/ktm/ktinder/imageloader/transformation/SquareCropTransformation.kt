package com.ktm.ktinder.imageloader.transformation

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlin.math.min

/**
 * User: Khang
 * Date: Tuesday, August 03 2021
 * Time: 23:14
 */
class SquareCropTransformation : Transformation {

    private companion object {
        val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override val key: String = SquareCropTransformation::class.java.name

    override suspend fun transform(originalBitmap: Bitmap): Bitmap {
        return withContext(Dispatchers.IO) {
            ensureActive()
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            val size = min(originalBitmap.width, originalBitmap.height)
            Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).applyCanvas {
                drawRect(0F, 0F, size.toFloat(), size.toFloat(), paint)
                drawBitmap(
                    originalBitmap,
                    (size - originalBitmap.width) / 2f,
                    (size - originalBitmap.height) / 2f,
                    paint.apply {
                        xfermode = XFERMODE
                    }
                )
            }
        }
    }
}