package com.example.project_simplrepair.Screen

import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
    onCancel: () -> Unit,
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val controller = remember { CameraCaptureController(context, executor) }
    var previewUseCase by remember { mutableStateOf<Preview?>(null) }

//    val sysUi = rememberSystemUiController()
//    // remember to import: import com.google.accompanist.systemuicontroller.rememberSystemUiController
//
//    // once when this screen enters composition, hide bars:
//    SideEffect {
//        sysUi.isSystemBarsVisible = false
//    }
//
//    // Optionally restore when you leave (could also be in a DisposableEffect):
//    DisposableEffect(Unit) {
//        onDispose {
//            sysUi.isSystemBarsVisible = true
//        }
//    }
    LaunchedEffect(Unit) {
        controller.cameraPreview(lifecycleOwner) { preview ->
            previewUseCase = preview
        }
    }

    Box(
        Modifier
            .fillMaxSize()
    ) {
        previewUseCase?.let { preview ->
            AndroidView({ context ->
                PreviewView(context).apply { preview.surfaceProvider = surfaceProvider }
            }, Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .width(240.dp)
                .aspectRatio(9f / 16f)
                .drawWithCache {
                    val strokeWidth = 2.dp.toPx()
                    val corner = 16.dp.toPx()               // tweak corner radius
                    // dash pattern: 26px on, 8px off
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(26f, 12f), 0f)

                    onDrawBehind {
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset.Zero,
                            size = size,                       // fill the full box
                            cornerRadius = CornerRadius(corner, corner),
                            style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                        )
                    }
                }
                .align(alignment = Alignment.Center)

        )



        // 2) cancel/back button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton (
                onClick = {  },

                ) {
                Icon(

                    imageVector = Icons.Default.Menu,
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
                    .size(96.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(64.dp),
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Take Photo"
                )
            }

            IconButton (
                onClick = onCancel,

                ) {
                Icon(

                    imageVector = Icons.Default.Check,
                    contentDescription = "Close Camera"
                )
            }
        }

        // 3) shutter button
    }
}


