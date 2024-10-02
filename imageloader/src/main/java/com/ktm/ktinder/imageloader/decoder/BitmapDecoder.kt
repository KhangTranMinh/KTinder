package com.ktm.ktinder.imageloader.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.ktm.ktinder.imageloader.logger.Logger
import com.ktm.ktinder.imageloader.logger.logDebug
import com.ktm.ktinder.imageloader.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File

class BitmapDecoder(private val logger: Logger?) {

    suspend fun decode(
        context: Context,
        reqWidth: Int,
        reqHeight: Int,
        data: DecodeData
    ): Bitmap? {
        return withContext(Dispatchers.IO) {
            ensureActive()
            kotlin.runCatching {
                when (data) {
                    is DecodeData.FileUri -> {
                        if (Const.URI_SCHEME_ASSETS == data.uri.scheme) {
                            decodeAsset(context, reqWidth, reqHeight, data)
                        } else {
                            decodeExternalFile(context, reqWidth, reqHeight, data)
                        }
                    }

                    is DecodeData.Resource -> {
                        decodeResource(context, reqWidth, reqHeight, data)
                    }

                    is DecodeData.CachedFile -> {
                        decodeCachedFile(reqWidth, reqHeight, data)
                    }
                }
            }.onFailure {
                logger?.logDebug("decode FAIL", it)
            }.getOrNull()
        }
    }

    private fun decodeAsset(
        context: Context,
        reqWidth: Int,
        reqHeight: Int,
        data: DecodeData.FileUri
    ): Bitmap? {
        logger?.logDebug("decode assets")
        val filePath = data.uri.path?.removePrefix("/") ?: ""
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            context.assets.open(filePath).use { stream ->
                BitmapFactory.decodeStream(stream, null, this)
            }
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
            context.assets.open(filePath).use { stream ->
                BitmapFactory.decodeStream(stream, null, this)
            }
        }
    }

    private fun decodeExternalFile(
        context: Context,
        reqWidth: Int,
        reqHeight: Int,
        data: DecodeData.FileUri
    ): Bitmap? {
        logger?.logDebug("decode external file")
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            context.contentResolver.openInputStream(data.uri).use { stream ->
                BitmapFactory.decodeStream(stream, null, this)
            }
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
            context.contentResolver.openInputStream(data.uri).use { stream ->
                BitmapFactory.decodeStream(stream, null, this)
            }
        }
    }

    private fun decodeResource(
        context: Context,
        reqWidth: Int,
        reqHeight: Int,
        data: DecodeData.Resource
    ): Bitmap? {
        logger?.logDebug("decode resource")
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, data.resId, this)
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
            BitmapFactory.decodeResource(context.resources, data.resId, this)
        }
    }

    private fun decodeCachedFile(
        reqWidth: Int,
        reqHeight: Int,
        data: DecodeData.CachedFile
    ): Bitmap? {
        logger?.logDebug("decode cached file")
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(data.file.absolutePath, this)
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(data.file.absolutePath, this)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }

        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        logger?.logDebug(
            "decode with inSampleSize: $inSampleSize \n" +
                    "reqW: $reqHeight | reqH: $reqHeight \n" +
                    "actualW: ${options.outWidth} | actualH: ${options.outHeight}"
        )
        return inSampleSize
    }

    sealed class DecodeData {

        class FileUri(val uri: Uri) : DecodeData()

        class Resource(val resId: Int) : DecodeData()

        class CachedFile(val file: File) : DecodeData()
    }
}