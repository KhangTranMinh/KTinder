package com.ktm.ktinder.imageloader.request

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import com.ktm.ktinder.imageloader.extension.getLifecycle
import com.ktm.ktinder.imageloader.transformation.Transformation
import com.ktm.ktinder.imageloader.util.hashKey

class ImageRequest private constructor(
    val context: Context,
    val imageView: ImageView,
    val data: RequestData,
    val transformation: Transformation?,
    val isCacheEnabled: Boolean,
    val placeholderResId: Int?,
    val errorResId: Int?,
) {

    val lifecycle: Lifecycle = context.getLifecycle()!!

    val cachedKey = hashKey(
        if (transformation == null) {
            data.key
        } else {
            "${data.key}_${transformation.key}"
        }
    )

    class Builder {

        private var _context: Context? = null
        private var _imageView: ImageView? = null
        private var _data: RequestData? = null
        private var transformation: Transformation? = null
        private var isCacheEnabled: Boolean = true
        private var placeholderResId: Int? = null
        private var errorResId: Int? = null

        fun with(context: Context) = apply {
            this._context = context
        }

        fun into(imageView: ImageView) = apply {
            this._imageView = imageView
        }

        fun data(data: RequestData) = apply {
            this._data = data
        }

        fun transformation(transformation: Transformation?) = apply {
            this.transformation = transformation
        }

        fun enableCache(isEnabled: Boolean) = apply {
            this.isCacheEnabled = isEnabled
        }

        fun placeholderResId(resId: Int?) = apply {
            this.placeholderResId = resId
        }

        fun errorResId(resId: Int?) = apply {
            this.errorResId = resId
        }

        fun build(): ImageRequest {
            val context = _context ?: throw Exception("The request's context is null.")
            val imageView = _imageView ?: throw Exception("The request's image view is null.")
            val data = _data ?: throw Exception("The request's data is null.")
            return ImageRequest(
                context,
                imageView,
                data,
                transformation,
                isCacheEnabled,
                placeholderResId,
                errorResId
            )
        }
    }

    sealed class RequestData {

        abstract val key: String

        class FileUri(
            val uri: Uri,
            override val key: String = hashKey(uri.toString())
        ) : RequestData()

        class Resource(
            val resId: Int,
            override val key: String = hashKey(resId.toString())
        ) : RequestData()

        class Url(
            val url: String,
            override val key: String = hashKey(url)
        ) : RequestData()
    }
}