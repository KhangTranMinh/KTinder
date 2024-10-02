package com.ktm.ktinder.imageloader.util

import android.view.View
import com.ktm.ktinder.imageloader.KTinderImageLoader
import com.ktm.ktinder.imageloader.request.ImageRequest
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

/**
 * User: Khang
 * Date: Sunday, August 01 2021
 * Time: 10:18
 */
class ViewStateListener : View.OnAttachStateChangeListener {

    private var imageLoaderRef: WeakReference<KTinderImageLoader>? = null
    private var imageRequestRef: WeakReference<ImageRequest>? = null
    private var requestJob: Job? = null

    // this will prevent onViewAttachedToWindow run at first time
    private var shouldSkipAttach = true

    fun setRequestJob(
        imageLoader: KTinderImageLoader,
        imageRequest: ImageRequest,
        job: Job
    ) {
        imageLoaderRef = WeakReference(imageLoader)
        imageRequestRef = WeakReference(imageRequest)
        requestJob?.cancel()
        requestJob = job
    }

    override fun onViewAttachedToWindow(view: View) {
        if (shouldSkipAttach) {
            shouldSkipAttach = false
            return
        }
        imageRequestRef?.get()?.let { imageLoaderRef?.get()?.loadImage(it) }
    }

    override fun onViewDetachedFromWindow(view: View) {
        shouldSkipAttach = false
        requestJob?.cancel()
    }
}