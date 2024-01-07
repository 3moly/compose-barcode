package com.threemoly.barcode

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

@Composable
actual fun Scanner(
    modifier: Modifier,
    onCodeScanned: (result: ScannerResult) -> Unit
) {
    val context = LocalContext.current
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { granted ->
//            hasCamPermission = granted
//        }
//    )
//    LaunchedEffect(key1 = true) {
//        launcher.launch(Manifest.permission.CAMERA)
//    }
    var scanFlag by remember {
        mutableStateOf(false)
    }
    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            capture.decode()
            this.decodeContinuous { result ->
                result.text?.let { barCodeOrQr ->
                    onCodeScanned(
                        ScannerResult(
                            code = barCodeOrQr,
                            barcodeType = result.barcodeFormat.name.toBarcodeType()
                        )
                    )
                    //Logger.d("code scanned $barCodeOrQr ${result.barcodeFormat.name}")
                    //Do something and when you finish this something
                    //put scanFlag = false to scan another item
                    scanFlag = false
                }
                //If you don't put this scanFlag = false, it will never work again.
                //you can put a delay over 2 seconds and then scanFlag = false to prevent multiple scanning
            }
        }
    }
    if (hasCamPermission) {
        AndroidView(
            modifier = modifier,
            factory = { compoundBarcodeView },
        )
        LaunchedEffect(key1 = Unit, block = {
            compoundBarcodeView.resume()
        })
    }
}