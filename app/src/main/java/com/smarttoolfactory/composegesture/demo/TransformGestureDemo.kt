package com.smarttoolfactory.composegesture.demo

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.composegesture.R
import com.smarttoolfactory.gesture.PointerRequisite
import com.smarttoolfactory.gesture.detectPointerTransformGestures
import com.smarttoolfactory.gesture.detectTransformGestures
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TransformMotionEventDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        println()

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "detectTransformGestures",
            fontSize = 18.sp,
            color = MaterialTheme.colors.primary
        )

        DetectTransformGesturesSample()
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "detectPointerTransformGestures",
            fontSize = 18.sp,
            color = MaterialTheme.colors.primary
        )
        DetectPointerTransformGesturesSample()
        Spacer(modifier = Modifier.height(40.dp))

    }
}

/**
 *  detectTransformGesturesAndChanges has callbacks to notify when gesture started, when it's on
 *  and when it's over.
 *  When gesture is on it returns same data detectTranformGestures does and also returns
 *  main pointer and all of the pointers that are down for transform gestures such as pinch,
 *  zoom or rotation
 */
@Composable
private fun DetectTransformGesturesSample() {
    val decimalFormat = remember { DecimalFormat("0.0") }

    var zoom by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var centroid by remember { mutableStateOf(Offset.Zero) }
    var angle by remember { mutableStateOf(0f) }

    var borderColor by remember { mutableStateOf(Color.LightGray) }

    var transformDetailText by remember {
        mutableStateOf(
            "Use pinch gesture to zoom, move image with single finger in " +
                    "either x or y coordinates.\n" +
                    "Rotate image using two fingers with twisting gesture."
        )
    }
    val imageModifier = Modifier
        .border(3.dp, borderColor)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures(
                onGestureStart = {
                    borderColor = Color.Yellow
                    transformDetailText = "GESTURE START"
                },
                onGesture = { gestureCentroid: Offset,
                              gesturePan: Offset,
                              gestureZoom: Float,
                              gestureRotate: Float,
                              mainPointerInputChange: PointerInputChange,
                              pointerList: List<PointerInputChange> ->
                    val oldScale = zoom
                    val newScale = zoom * gestureZoom

                    // For natural zooming and rotating, the centroid of the gesture should
                    // be the fixed point where zooming and rotating occurs.
                    // We compute where the centroid was (in the pre-transformed coordinate
                    // space), and then compute where it will be after this delta.
                    // We then compute what the new offset should be to keep the centroid
                    // visually stationary for rotating and zooming, and also apply the pan.
                    offset = (offset + gestureCentroid / oldScale).rotateBy(gestureRotate) -
                            (gestureCentroid / newScale + gesturePan / oldScale)
                    zoom = newScale.coerceIn(0.5f..5f)
                    angle += gestureRotate

                    centroid = gestureCentroid
                    transformDetailText =
                        "PointerInputChange list: ${pointerList.size}\n" +
                                "Zoom: ${decimalFormat.format(zoom)}, centroid: $gestureCentroid\n" +
                                "angle: ${decimalFormat.format(angle)}, " +
                                "Rotate: ${decimalFormat.format(gestureRotate)}, pan: $gesturePan"

                    borderColor = Color.Green
                },
                onGestureEnd = {
                    borderColor = Color.LightGray
                    transformDetailText = "GESTURE END"
                }
            )
        }
        .drawWithContent {
            drawContent()
            drawCircle(color = Color.Red, center = centroid, radius = 20f)
        }
        .graphicsLayer {
            translationX = -offset.x * zoom
            translationY = -offset.y * zoom
            scaleX = zoom
            scaleY = zoom
            rotationZ = angle
            TransformOrigin(0f, 0f).also { transformOrigin = it }
        }

    ImageBox(boxModifier, imageModifier, R.drawable.landscape, transformDetailText)
}

@Composable
private fun DetectPointerTransformGesturesSample() {
    val decimalFormat = remember { DecimalFormat("0.0") }

    var zoom by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var centroid by remember { mutableStateOf(Offset.Zero) }
    var activePointer by remember { mutableStateOf(Offset.Zero) }
    var angle by remember { mutableStateOf(0f) }

    var borderColor by remember { mutableStateOf(Color.LightGray) }

    var transformDetailText by remember {
        mutableStateOf(
            "Use pinch gesture to zoom, move image with single finger in " +
                    "either x or y coordinates.\n" +
                    "Rotate image using two fingers with twisting gesture."
        )
    }
    val imageModifier = Modifier
        .border(3.dp, borderColor)
        .fillMaxSize()
        .pointerInput(Unit) {
            detectPointerTransformGestures(
                numberOfPointers = 1,
                requisite = PointerRequisite.GreaterThan,
                onGestureStart = {
                    borderColor = Color.Yellow
                    transformDetailText = "GESTURE START"
                },
                onGesture = { gestureCentroid: Offset,
                              gesturePan: Offset,
                              gestureZoom: Float,
                              gestureRotate: Float,
                              mainPointerInputChange: PointerInputChange,
                              pointerList: List<PointerInputChange> ->
                    val oldScale = zoom
                    val newScale = zoom * gestureZoom

                    // For natural zooming and rotating, the centroid of the gesture should
                    // be the fixed point where zooming and rotating occurs.
                    // We compute where the centroid was (in the pre-transformed coordinate
                    // space), and then compute where it will be after this delta.
                    // We then compute what the new offset should be to keep the centroid
                    // visually stationary for rotating and zooming, and also apply the pan.
                    offset = (offset + gestureCentroid / oldScale).rotateBy(gestureRotate) -
                            (gestureCentroid / newScale + gesturePan / oldScale)
                    zoom = newScale.coerceIn(0.5f..5f)
                    angle += gestureRotate

                    centroid = gestureCentroid

                    activePointer = mainPointerInputChange.position

                    transformDetailText =
                        "Number of pointers: ${pointerList.size}\n" +
                                "Zoom: ${decimalFormat.format(zoom)}, centroid: $gestureCentroid\n" +
                                "angle: ${decimalFormat.format(angle)}, " +
                                "Rotate: ${decimalFormat.format(gestureRotate)}, pan: $gesturePan"

                    borderColor = Color.Green
                },
                onGestureEnd = {
                    borderColor = Color.LightGray
                    transformDetailText = "GESTURE END"
                },
                onGestureCancel = {
                    borderColor = Color.LightGray
                    transformDetailText = "GESTURE CANCEL"
                }
            )
        }
        .drawWithContent {
            drawContent()
            drawCircle(color = Color.Red, center = centroid, radius = 20f)
            drawCircle(color = Color.Blue, center = activePointer, radius = 20f)
        }
        .graphicsLayer {
            translationX = -offset.x * zoom
            translationY = -offset.y * zoom
            scaleX = zoom
            scaleY = zoom
            rotationZ = angle
            TransformOrigin(0f, 0f).also { transformOrigin = it }
        }

    ImageBox(boxModifier, imageModifier, R.drawable.landscape, transformDetailText)
}


/**
 * Rotates the given offset around the origin by the given angle in degrees.
 *
 * A positive angle indicates a counterclockwise rotation around the right-handed 2D Cartesian
 * coordinate system.
 *
 * See: [Rotation matrix](https://en.wikipedia.org/wiki/Rotation_matrix)
 */
fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadians = angle * PI / 180
    return Offset(
        (x * cos(angleInRadians) - y * sin(angleInRadians)).toFloat(),
        (x * sin(angleInRadians) + y * cos(angleInRadians)).toFloat()
    )
}

private const val PI = Math.PI

@Composable
fun ImageBox(
    modifier: Modifier,
    imageModifier: Modifier,
    imageRes: Int,
    text: String,
    color: Color = Color.Red
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        Text(
            text = text,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x66000000))
                .padding(vertical = 2.dp)
                .align(Alignment.BottomStart)
        )
    }
}

val boxModifier = Modifier
    .fillMaxWidth()
    .height(250.dp)
    .clipToBounds()
    .background(Color.LightGray)
