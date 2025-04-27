package com.example.project_simplrepair.Screen

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.project_simplrepair.Camera.CameraCaptureController
import java.util.concurrent.Executors


@Composable
fun CameraScreen(
    onPhotoTaken: (String) -> Unit,
    onCancel: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val controller = remember { CameraCaptureController(context, executor) }
    var previewUseCase by remember { mutableStateOf<Preview?>(null) }

    LaunchedEffect(Unit) {
        controller.cameraPreview(lifecycleOwner) { preview ->
            previewUseCase = preview
        }
    }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        previewUseCase?.let { pu ->
            AndroidView({ ctx ->
                PreviewView(ctx).apply { pu.surfaceProvider = surfaceProvider }
            }, Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .drawWithCache {
                    // Build the rounded polygon once
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 4,
                        radius      = size.minDimension / 2,
                        centerX     = size.width / 2,
                        centerY     = size.height / 2,
                        rounding = CornerRounding(32f)
                    )
                    val path = roundedPolygon.toPath().asComposePath()

                    // Create a dash pattern: 8px on, 8px off
                    val dashEffect =
                        androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(26f, 8f), 0f)

                    onDrawBehind {
                        // Draw only the outline as a dashed stroke
                        drawPath(
                            path  = path,
                            color = Color.White,
                            style = Stroke(
                                width      = 4.dp.toPx(),
                                pathEffect = dashEffect
                            )
                        )
                    }
                }
                .fillMaxSize()
                .aspectRatio(16f / 9f)
        )

        // 2) cancel/back button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {

            IconButton (
                onClick = onCancel,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Camera"
                )
            }
            IconButton(
                onClick = {
                    controller.takePhoto(
                        onPhotoTaken = { path -> onPhotoTaken(path) },
                        onError = { /* handle error */ }
                    )
                },
                modifier = Modifier
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Take Photo"
                )
            }
        }

        // 3) shutter button
    }
}
