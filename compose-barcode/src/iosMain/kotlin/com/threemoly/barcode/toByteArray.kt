package com.threemoly.barcode

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

internal fun UIImage.toByteArray(): ByteArray? {
    return (UIImagePNGRepresentation(this) ?: return null).toByteArray()
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(nsData: NSData = this): ByteArray {
    return ByteArray(nsData.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), nsData.bytes, nsData.length)
        }
    }
}