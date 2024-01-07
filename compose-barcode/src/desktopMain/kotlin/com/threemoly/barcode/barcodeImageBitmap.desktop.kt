package com.threemoly.barcode

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Scanner(
    modifier: Modifier,
    onCodeScanned: (result: ScannerResult) -> Unit
) {
}