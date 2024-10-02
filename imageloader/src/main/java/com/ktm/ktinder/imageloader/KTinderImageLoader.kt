package com.ktm.ktinder.imageloader

import android.content.Context
import android.graphics.Bitmap
import com.ktm.ktinder.imageloader.cache.disk.DefaultDiskCache
import com.ktm.ktinder.imageloader.cache.disk.DiskCache
import com.ktm.ktinder.imageloader.cache.disk.NoOpDiskCache
import com.ktm.ktinder.imageloader.cache.mem.DefaultMemCache
import com.ktm.ktinder.imageloader.cache.mem.NoOpMemCache
import com.ktm.ktinder.imageloader.cache.mem.MemCache
import com.ktm.ktinder.imageloader.decoder.BitmapDecoder
import com.ktm.ktinder.imageloader.downloader.HttpDownloader
import com.ktm.ktinder.imageloader.extension.awaitStarted
import com.ktm.ktinder.imageloader.extension.stateListener
import com.ktm.ktinder.imageloader.logger.DebugLogger
import com.ktm.ktinder.imageloader.logger.Logger
import com.ktm.ktinder.imageloader.logger.logDebug
import com.ktm.ktinder.imageloader.request.ImageRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class KTinderImageLoader private constructor(
    context: Context,
    private val logger: Logger?
) {

    companion object {
        @Volatile
        private var instance: KTinderImageLoader? = null

        fun get(
            context: Context,
            logger: Logger? = DebugLogger("t3st")
        ): KTinderImageLoader {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = KTinderImageLoader(context.applicationContext, logger)
                    }
                }
            }
            return instance!!
        }
    }

    private val coroutineScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main.immediate +
                CoroutineExceptionHandler { _, throwable ->
                    logger?.logDebug(throwable)
                })

    private val defaultMemCache = DefaultMemCache(logger)
    private val noOpMemCache = NoOpMemCache()

    private val defaultDiskCache = DefaultDiskCache(logger, context.applicationContext)
    private val noOpDiskCache = NoOpDiskCache(logger, context.applicationContext)

    private val bitmapDecoder = BitmapDecoder(logger)

    private val httpDownloader = HttpDownloader(logger)

    fun loadImage(imageRequest: ImageRequest) {
        logger?.logDebug("load image")
        val job = coroutineScope.launch(Dispatchers.Main) {
            kotlin.runCatching {
                // wait for the view is ready
                imageRequest.lifecycle.awaitStarted()
                imageRequest.placeholderResId?.let {
                    imageRequest.imageView.setImageResource(it)
                }
                // get correct mem cache
                val memCache = requestMemCache(imageRequest)
                // get bitmap from mem cache
                val cachedBitmap = memCache.getBitmap(imageRequest.cachedKey)
                if (cachedBitmap == null) {
                    // no cached bitmap, process image request (download, decode)
                    val bitmap = processImageRequest(imageRequest)
                    if (bitmap == null) {
                        imageRequest.errorResId?.let {
                            imageRequest.imageView.setImageResource(it)
                        }
                    } else {
                        // transform bitmap if needed
                        val transformedBitmap =
                            imageRequest.transformation?.transform(bitmap) ?: bitmap
                        // update mem cache after decoding
                        memCache.putBitmap(imageRequest.cachedKey, transformedBitmap)
                        imageRequest.imageView.setImageBitmap(transformedBitmap)
                    }
                } else {
                    logger?.logDebug("load cached bitmap with key: ${imageRequest.cachedKey}")
                    imageRequest.imageView.setImageBitmap(cachedBitmap)
                }
            }.onFailure {
                logger?.logDebug("load image FAIL", it)
            }
        }
        // assign the job to view listener in order to cancel/restart job
        imageRequest.imageView.stateListener.setRequestJob(
            this,
            imageRequest,
            job
        )
    }

    private suspend fun processImageRequest(imageRequest: ImageRequest): Bitmap? {
        suspend fun decodeBitmap(decodeData: BitmapDecoder.DecodeData): Bitmap? {
            return bitmapDecoder.decode(
                imageRequest.context,
                imageRequest.imageView.width,
                imageRequest.imageView.height,
                decodeData
            )
        }

        return when (imageRequest.data) {
            is ImageRequest.RequestData.FileUri -> {
                decodeBitmap(BitmapDecoder.DecodeData.FileUri(imageRequest.data.uri))
            }

            is ImageRequest.RequestData.Resource -> {
                decodeBitmap(BitmapDecoder.DecodeData.Resource(imageRequest.data.resId))
            }

            is ImageRequest.RequestData.Url -> {
                val diskCache = requestDiskCache(imageRequest)
                val key = imageRequest.data.key
                val cachedFile = diskCache.getCachedFile(key)
                if (diskCache.isFileExisted(cachedFile.absolutePath)) {
                    logger?.logDebug("found cached file with key: $key")
                } else {
                    // no cached file, download the image into disk cache
                    val isDownloadSuccess =
                        httpDownloader.download(imageRequest.data.url, cachedFile)
                    if (isDownloadSuccess) {
                        logger?.logDebug("download completed with key: $key")
                    } else {
                        diskCache.deleteCacheFile(cachedFile)
                    }
                    // trim disk cache size if needed
                    diskCache.invalidateCache()
                }
                decodeBitmap(BitmapDecoder.DecodeData.CachedFile(cachedFile))
            }
        }
    }

    private fun requestMemCache(imageRequest: ImageRequest? = null): MemCache {
        return imageRequest?.let {
            if (it.isCacheEnabled) defaultMemCache else noOpMemCache
        } ?: defaultMemCache
    }

    private fun requestDiskCache(imageRequest: ImageRequest? = null): DiskCache {
        return imageRequest?.let {
            if (it.isCacheEnabled) defaultDiskCache else noOpDiskCache
        } ?: defaultDiskCache
    }

    fun clearMemCache() = requestMemCache().clearCache()

    fun clearDiskCache() {
        coroutineScope.launch(Dispatchers.Main) {
            requestDiskCache().clearCache()
        }
    }
}