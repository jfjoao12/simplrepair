package com.example.project_simplrepair.Operations

import android.graphics.Bitmap
import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun saveBitmapToInternalStorage(bitmap: Bitmap, context: Context): String {
    val dir = File(context.filesDir, "MyAppPhotos").apply { if (!exists()) mkdirs() }
    val name = "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        .format(Date()) + ".jpg"
    val file = File(dir, name)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return file.absolutePath
}