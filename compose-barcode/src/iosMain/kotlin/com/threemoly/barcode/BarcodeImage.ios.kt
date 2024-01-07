package com.threemoly.barcode

import androidx.compose.ui.graphics.ImageBitmap
import cocoapods.ZXingObjC.ZXBarcodeFormat
import cocoapods.ZXingObjC.ZXImage
import cocoapods.ZXingObjC.ZXMultiFormatWriter
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGImageRelease
import platform.CoreGraphics.CGImageRetain
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
actual fun barcodeImageBitmap(
    number: String,
    barcodeFormat: BarcodeType,
    width: Int,
    height: Int
): ImageBitmap? {
    val barcode = when (barcodeFormat) {
        BarcodeType.AZTEC -> ZXBarcodeFormat.kBarcodeFormatAztec
        BarcodeType.CODABAR -> ZXBarcodeFormat.kBarcodeFormatCodabar
        BarcodeType.CODE_39 -> ZXBarcodeFormat.kBarcodeFormatCode39
        BarcodeType.CODE_93 -> ZXBarcodeFormat.kBarcodeFormatCode93
        BarcodeType.CODE_128 -> ZXBarcodeFormat.kBarcodeFormatCode128
        BarcodeType.DATA_MATRIX -> ZXBarcodeFormat.kBarcodeFormatDataMatrix
        BarcodeType.EAN_8 -> ZXBarcodeFormat.kBarcodeFormatEan8
        BarcodeType.EAN_13 -> ZXBarcodeFormat.kBarcodeFormatEan13
        BarcodeType.ITF -> ZXBarcodeFormat.kBarcodeFormatITF
        BarcodeType.MAXICODE -> ZXBarcodeFormat.kBarcodeFormatMaxiCode
        BarcodeType.PDF_417 -> ZXBarcodeFormat.kBarcodeFormatPDF417
        BarcodeType.QR_CODE -> ZXBarcodeFormat.kBarcodeFormatQRCode
        BarcodeType.RSS_14 -> ZXBarcodeFormat.kBarcodeFormatRSS14
        BarcodeType.RSS_EXPANDED -> ZXBarcodeFormat.kBarcodeFormatRSSExpanded
        BarcodeType.UPC_A -> ZXBarcodeFormat.kBarcodeFormatUPCA
        BarcodeType.UPC_E -> ZXBarcodeFormat.kBarcodeFormatUPCE
        BarcodeType.UPC_EAN_EXTENSION -> ZXBarcodeFormat.kBarcodeFormatUPCEANExtension
    }
    val writer = ZXMultiFormatWriter()
    val zxImage = try {
        val result = writer.encode(
            contents = number,
            format = barcode,
            width = width,
            height = height,
            error = null
        )
        ZXImage.imageWithMatrix(result)
    } catch (exc: Exception) {
        null
    }
    val imageRetain = CGImageRetain(zxImage?.cgimage)
    val bytes = UIImage(imageRetain).toByteArray()

    CGImageRelease(imageRetain)
    return if (bytes != null)
        getImageBitmapFromBytes(
            bytes
        ) else null
}

