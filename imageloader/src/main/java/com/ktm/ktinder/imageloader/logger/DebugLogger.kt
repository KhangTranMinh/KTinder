package com.ktm.ktinder.imageloader.logger

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * User: Khang
 * Date: Saturday, July 31 2021
 * Time: 21:45
 */
class DebugLogger(override val logTag: String) : Logger {

    override fun log(priority: Int, message: String?, throwable: Throwable?) {
        message?.let { Log.println(priority, logTag, it) }
        throwable?.let {
            val writer = StringWriter().apply {
                it.printStackTrace(PrintWriter(this))
            }
            Log.println(priority, logTag, writer.toString())
        }
    }
}