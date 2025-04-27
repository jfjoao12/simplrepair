package com.example.project_simplrepair.Camera

import android.app.Activity
import android.content.Context
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor

/**
 * Controller to manage CameraX preview and image capture,
 * saving photos to internal storage and returning file paths.
 */
class CameraCaptureController(
    private val context: Context,
    private val cameraExecutor: Executor
) {
    private var imageCapture: ImageCapture? = null

    /**
     * Starts the camera, binding the Preview and ImageCapture use cases.
     */
    fun cameraPreview(owner: LifecycleOwner, onReady: (Preview) -> Unit) {
        // Determine device rotation for proper orientation
        val rotation = (context as? Activity)
            ?.windowManager
            ?.defaultDisplay
            ?.rotation
            ?: Surface.ROTATION_0

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Build Preview use-case with target rotation
            val preview = Preview.Builder()
                .setTargetRotation(rotation)
                .build()

            // Build ImageCapture use-case with target rotation
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(rotation)
                .build()

            // Unbind any existing use-cases
            cameraProvider.unbindAll()

            // Bind use-cases to lifecycle
            cameraProvider.bindToLifecycle(
                owner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )

            // Notify UI to attach preview's surface provider
            onReady(preview)
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * Captures a photo, saves it internally, and returns the file path.
     */
    fun takePhoto(
        onPhotoTaken: (String) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val capture = imageCapture ?: return

        // Prepare output directory in internal storage
        val photoDir = File(context.filesDir, "MyAppPhotos").apply {
            if (!exists()) mkdirs()
        }

        // Create a timestamped filename
        val filename = SimpleDateFormat(
            "yyyyMMdd_HHmmss_SSS",
            Locale.US
        ).format(Date()) + ".jpg"
        val file = File(photoDir, filename)

        // Wrap in OutputFileOptions
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(file)
            .build()

        // Trigger image capture
        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onPhotoTaken(file.absolutePath)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
    }
}
