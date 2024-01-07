package com.threemoly.barcode

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

actual fun barcodeImageBitmap(
    number: String,
    barcodeFormat: BarcodeType,
    width: Int,
    height: Int
): ImageBitmap? {
    val barcodeFormatType = when (barcodeFormat) {
        BarcodeType.AZTEC -> BarcodeFormat.AZTEC
        BarcodeType.CODABAR -> BarcodeFormat.CODABAR
        BarcodeType.CODE_39 -> BarcodeFormat.CODE_39
        BarcodeType.CODE_93 -> BarcodeFormat.CODE_93
        BarcodeType.CODE_128 -> BarcodeFormat.CODE_128
        BarcodeType.DATA_MATRIX -> BarcodeFormat.DATA_MATRIX
        BarcodeType.EAN_8 -> BarcodeFormat.EAN_8
        BarcodeType.EAN_13 -> BarcodeFormat.EAN_13
        BarcodeType.ITF -> BarcodeFormat.ITF
        BarcodeType.MAXICODE -> BarcodeFormat.MAXICODE
        BarcodeType.PDF_417 -> BarcodeFormat.PDF_417
        BarcodeType.QR_CODE -> BarcodeFormat.QR_CODE
        BarcodeType.RSS_14 -> BarcodeFormat.RSS_14
        BarcodeType.RSS_EXPANDED -> BarcodeFormat.RSS_EXPANDED
        BarcodeType.UPC_A -> BarcodeFormat.UPC_A
        BarcodeType.UPC_E -> BarcodeFormat.UPC_E
        BarcodeType.UPC_EAN_EXTENSION -> BarcodeFormat.UPC_EAN_EXTENSION
        else -> BarcodeFormat.QR_CODE
    }
    return try {
        val qrCodeBitmap = BarcodeEncoder().encodeBitmap(
            number,
            barcodeFormatType,
            width,
            height
        )
        qrCodeBitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}