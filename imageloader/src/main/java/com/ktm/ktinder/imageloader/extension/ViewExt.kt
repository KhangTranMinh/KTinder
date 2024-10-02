package com.ktm.ktinder.imageloader.extension

import android.net.Uri
import android.widget.ImageView
import com.ktm.ktinder.imageloader.KTinderImageLoader
import com.ktm.ktinder.imageloader.R
import com.ktm.ktinder.imageloader.request.ImageRequest
import com.ktm.ktinder.imageloader.util.ViewStateListener

fun ImageView.load(
    fileUri: Uri,
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    loadAny(fileUri, builder)
}

fun ImageView.load(
    resId: Int,
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    loadAny(resId, builder)
}

fun ImageView.load(
    url: String,
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    loadAny(url, builder)
}

private fun ImageView.loadAny(
    source: Any?,
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    val data = when (source) {
        is Uri -> ImageRequest.RequestData.FileUri(source)
        is Int -> ImageRequest.RequestData.Resource(source)
        is String -> ImageRequest.RequestData.Url(source)
        else -> throw Exception("Source is not supported")
    }
    val imageRequest = ImageRequest.Builder()
        .with(context)
        .data(data)
        .apply(builder)
        .into(this)
        .build()
    KTinderImageLoader.get(context).loadImage(imageRequest)
}

internal val ImageView.stateListener: ViewStateListener
    get() {
        var listener = getTag(R.id.view_state_listener) as? ViewStateListener
        if (listener == null) {
            listener = synchronized(this) {
                // Check again in case tag was just set.
                (getTag(R.id.view_state_listener) as? ViewStateListener)?.let {
                    return@synchronized it
                }
                ViewStateListener().apply {
                    addOnAttachStateChangeListener(this)
                    setTag(R.id.view_state_listener, this)
                }
            }
        }
        return listener
    }