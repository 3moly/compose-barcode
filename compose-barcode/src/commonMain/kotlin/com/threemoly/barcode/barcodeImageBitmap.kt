package com.threemoly.barcode

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

expect fun barcodeImageBitmap(
    number: String,
    barcodeFormat: BarcodeType,
    width: Int,
    height: Int
): ImageBitmap?

data class ScannerResult(
    val code: String,
    val barcodeType: BarcodeType
)

@Composable
expect fun Scanner(modifier: Modifier, onCodeScanned: (result: ScannerResult) -> Unit)