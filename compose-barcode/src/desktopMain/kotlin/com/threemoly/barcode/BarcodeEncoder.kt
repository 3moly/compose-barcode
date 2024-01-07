package com.threemoly.barcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorInfo
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo

/**
 * Helper class for encoding barcodes as a Bitmap.
 *
 * Adapted from QRCodeEncoder, from the zxing project:
 * https://github.com/zxing/zxing
 *
 * Licensed under the Apache License, Version 2.0.
 */
internal class BarcodeEncoder {
    private fun createBitmap(matrix: BitMatrix): Bitmap {
        val width: Int = matrix.width
        val height: Int = matrix.height
        val pixels = ByteArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                val pixel = matrix.get(x, y)
                val color = if (pixel) {
                    BLACK
                } else {
                    WHITE
                }
                pixels[offset + x] = (color).toByte()
            }
        }
        val imageInfo = ImageInfo(
            width = width,
            height = height,
            colorInfo = ColorInfo(
                colorType = ColorType.GRAY_8,
                alphaType = ColorAlphaType.OPAQUE,
                colorSpace = ColorSpace.sRGB
            ),
        )
        val bitmap: Bitmap = Bitmap().apply {
            if(!allocPixels(imageInfo)){
                throw Exception("alloc pixels")
            }
        }
        bitmap.installPixels(pixels)
        return bitmap
    }

    @Throws(WriterException::class)
    fun encode(contents: String?, format: BarcodeFormat?, width: Int, height: Int): BitMatrix {
        return try {
            MultiFormatWriter().encode(contents, format, width, height)
        } catch (e: WriterException) {
            throw e
        } catch (e: Exception) {
            // ZXing sometimes throws an IllegalArgumentException
            throw WriterException(e)
        }
    }

    @Throws(WriterException::class)
    fun encode(
        contents: String?,
        format: BarcodeFormat?,
        width: Int,
        height: Int,
        hints: Map<EncodeHintType?, *>?
    ): BitMatrix {
        return try {
            MultiFormatWriter().encode(contents, format, width, height, hints)
        } catch (e: WriterException) {
            throw e
        } catch (e: Exception) {
            throw WriterException(e)
        }
    }

    @Throws(WriterException::class)
    fun encodeBitmap(contents: String?, format: BarcodeFormat?, width: Int, height: Int): Bitmap {
        return createBitmap(encode(contents, format, width, height))
    }

    @Throws(WriterException::class)
    fun encodeBitmap(
        contents: String?,
        format: BarcodeFormat?,
        width: Int,
        height: Int,
        hints: Map<EncodeHintType?, *>?
    ): Bitmap {
        return createBitmap(encode(contents, format, width, height, hints))
    }

    companion object {
        private const val WHITE = -0x1
        private const val BLACK = -0x1000000
    }
}
