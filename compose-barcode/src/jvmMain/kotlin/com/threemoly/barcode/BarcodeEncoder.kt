package com.threemoly.barcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import org.jetbrains.skia.Bitmap

/**
 * Helper class for encoding barcodes as a Bitmap.
 *
 * Adapted from QRCodeEncoder, from the zxing project:
 * https://github.com/zxing/zxing
 *
 * Licensed under the Apache License, Version 2.0.
 */
class BarcodeEncoder {
    fun createBitmap(matrix: BitMatrix): Bitmap {
        val width: Int = matrix.getWidth()
        val height: Int = matrix.getHeight()
        val pixels = ByteArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = (if (matrix.get(x, y)) BLACK else WHITE).toByte()
            }
        }

        //val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val bitmap: Bitmap = Bitmap().apply {

        }
        bitmap.installPixels(pixels = pixels)

        //bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
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
