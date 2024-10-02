package com.ktm.ktinder.imageloader.logger

import android.util.Log

interface Logger {

    val logTag: String

    /**
     * Write [message] and/or [throwable] to a logging destination.
     */
    fun log(priority: Int, message: String?, throwable: Throwable?)
}

internal fun Logger.logDebug(throwable: Throwable) {
    log(Log.ERROR, null, throwable)
}

internal fun Logger.logDebug(message: String) {
    log(Log.DEBUG, message, null)
}

internal fun Logger.logDebug(message: String, throwable: Throwable?) {
    log(Log.DEBUG, message, throwable)
}