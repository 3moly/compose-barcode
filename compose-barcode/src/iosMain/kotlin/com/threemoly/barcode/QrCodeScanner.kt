package com.threemoly.barcode

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaType
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObjectTypeAztecCode
import platform.AVFoundation.AVMetadataObjectTypeCode128Code
import platform.AVFoundation.AVMetadataObjectTypeCode39Code
import platform.AVFoundation.AVMetadataObjectTypeCode39Mod43Code
import platform.AVFoundation.AVMetadataObjectTypeCode93Code
import platform.AVFoundation.AVMetadataObjectTypeDataMatrixCode
import platform.AVFoundation.AVMetadataObjectTypeEAN13Code
import platform.AVFoundation.AVMetadataObjectTypeEAN8Code
import platform.AVFoundation.AVMetadataObjectTypeITF14Code
import platform.AVFoundation.AVMetadataObjectTypePDF417Code
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.AVFoundation.AVMetadataObjectTypeUPCECode
import platform.CoreGraphics.CGRect
import platform.Foundation.NSCoder
import platform.QuartzCore.CALayer
import platform.UIKit.UIView
import platform.darwin.dispatch_queue_main_t

class QrCodeScanner : UIView(), AVCaptureMetadataOutputObjectsDelegateProtocol {

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection
    ) {
        super.captureOutput(output, didOutputMetadataObjects, fromConnection)

        //todo stopScanning()

        val metadataObject = didOutputMetadataObjects.first()
        val readableObject = metadataObject as? AVMetadataMachineReadableCodeObject ?: return
        val stringValue = readableObject.stringValue ?: return
        found(stringValue)
        qrCodeSuccessAction?.invoke(stringValue, readableObject.type().toString())
    }

    private var captureSession: AVCaptureSession? = null
    private var qrCodeSuccessAction: ((String, String) -> Unit)? = null
    var isRunning: Boolean = captureSession?.isRunning() ?: false

    init {
        doInitialSetup()
    }

    @Deprecated(
        "Use constructor instead",
        replaceWith = ReplaceWith("UIView(coder)"),
        level = DeprecationLevel.ERROR
    )
    override fun initWithCoder(coder: NSCoder): UIView? {
        doInitialSetup()
        return UIView(coder)
    }

    @Deprecated(
        "Use constructor instead",
        replaceWith = ReplaceWith("UIView(frame)"),
        level = DeprecationLevel.ERROR
    )
    @OptIn(ExperimentalForeignApi::class)
    override fun initWithFrame(frame: CValue<CGRect>): UIView {
        doInitialSetup()
        return UIView(frame)
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun doInitialSetup() {
        clipsToBounds = true
        captureSession = AVCaptureSession()

        val videoCaptureDevice =
            AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return
        val videoInput: AVCaptureDeviceInput
        try {
            videoInput = AVCaptureDeviceInput(videoCaptureDevice, null)
        } catch (exc: Exception) {
            return
        }
        if (captureSession?.canAddInput(videoInput) == true) {
            captureSession?.addInput(videoInput)
        } else {
            scanningDidFail()
        }
        val metadataOutput = AVCaptureMetadataOutput()
        if (captureSession?.canAddOutput(metadataOutput) == true) {
            captureSession?.addOutput(metadataOutput)

            metadataOutput.setMetadataObjectsDelegate(this, dispatch_queue_main_t())
            metadataOutput.metadataObjectTypes = listOf(
                AVMetadataObjectTypeAztecCode,
                AVMetadataObjectTypeCode39Code,
                AVMetadataObjectTypeCode93Code,
                AVMetadataObjectTypeCode128Code,
                AVMetadataObjectTypeDataMatrixCode,
                AVMetadataObjectTypeEAN8Code,
                AVMetadataObjectTypeEAN13Code,
                AVMetadataObjectTypeITF14Code,
                AVMetadataObjectTypeCode39Mod43Code,
                AVMetadataObjectTypePDF417Code,
                AVMetadataObjectTypeUPCECode,
                AVMetadataObjectTypeQRCode
            )
        } else {
            scanningDidFail()
            return
        }

        (layer as AVCaptureVideoPreviewLayer).apply {
            session = captureSession
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }

        captureSession?.startRunning()
    }

    override fun layer(): CALayer {
        return AVCaptureVideoPreviewLayer.layer()
    }

    fun setQrCodeSuccess(action: (String, String) -> Unit) {
        qrCodeSuccessAction = action
    }

    fun scanningDidFail() {
        //delegate?.qrScanningDidFail()
        captureSession = null
    }

    fun found(code: String) {
        //delegate?.qrScanningSucceededWithCode(code)
    }

    fun stopScanning() {
        captureSession?.stopRunning()
    }

    fun startScanning() {
        captureSession?.startRunning()
    }

    companion object {
        fun makeView(action: (String, String) -> Unit): UIView {
            val scanner = QrCodeScanner()

            scanner.setQrCodeSuccess { tam, tam2 ->
                action(tam, tam2)
            }
            scanner.startScanning()
            return scanner
        }
    }
}