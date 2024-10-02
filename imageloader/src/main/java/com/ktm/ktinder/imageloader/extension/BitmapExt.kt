package com.ktm.ktinder.imageloader.extension

import android.graphics.Bitmap

internal fun Bitmap?.sizeInKB() = (this?.byteCount ?: 0) / 1024