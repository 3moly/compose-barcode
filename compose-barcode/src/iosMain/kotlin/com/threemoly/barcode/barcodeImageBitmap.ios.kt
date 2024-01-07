package com.threemoly.barcode

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureSession

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun Scanner(
    modifier: Modifier,
    onCodeScanned: (result: ScannerResult) -> Unit
) {
    val ioScope = rememberCoroutineScope()
    val qrCodeScanner = remember{
        QrCodeScanner(QrCodeScanner.emptyView())
    }
    val scannerView = remember {
        QrCodeScanner.makeView(qrCodeScanner)  { code, barcodeFormat ->

            val barcodeType = when (barcodeFormat) {
                "org.iso.Code128" -> BarcodeType.CODE_128
                "org.iso.Code39" -> BarcodeType.CODE_39
                "org.iso.Code93" -> BarcodeType.CODE_93
                "org.iso.QRCode" -> BarcodeType.QR_CODE
                "org.iso.PDF417" -> BarcodeType.PDF_417
                "org.iso.Aztec" -> BarcodeType.AZTEC
                "org.iso.DataMatrix" -> BarcodeType.DATA_MATRIX
                else -> BarcodeType.QR_CODE
            }
            onCodeScanned(ScannerResult(code = code, barcodeType = barcodeType))
        }
    }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            scannerView
        },
        update = {}
    )
}