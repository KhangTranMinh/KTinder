package com.ktm.ktinder.imageloader.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun hashKey(value: String) =
    try {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(value.toByteArray())
        bytesToHexString(messageDigest.digest())
    } catch (e: NoSuchAlgorithmException) {
        value.hashCode().toString()
    }

private fun bytesToHexString(bytes: ByteArray): String {
    val sb = StringBuilder()
    for (i in bytes.indices) {
        val hex = Integer.toHexString(0xFF and bytes[i].toInt())
        if (hex.length == 1) sb.append('0')
        sb.append(hex)
    }
    return sb.toString()
}

fun Int.withSeparator(): String = "%,d".format(this)

fun Long.withSeparator(): String = "%,d".format(this)
