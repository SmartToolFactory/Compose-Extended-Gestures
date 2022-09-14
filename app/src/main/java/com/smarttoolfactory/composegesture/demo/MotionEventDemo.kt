package com.smarttoolfactory.composegesture.demo

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.gesture.MotionEvent
import com.smarttoolfactory.gesture.pointerMotionEventList
import com.smarttoolfactory.gesture.pointerMotionEvents

@Composable
fun MoveMotionEventDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffECEFF1))
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {

        Text("pointerMotionEvents", fontSize = 18.sp, color = MaterialTheme.colors.primary)
        PointerMotionEventsSample()
        Spacer(modifier = Modifier.height(30.dp))
        Text("pointerMotionEventList", fontSize = 18.sp, color = MaterialTheme.colors.primary)
        PointerMotionEventListSample()
    }
}

@Composable
private fun PointerMotionEventsSample() {

    // This is motion state. Initially or when touch is completed state is at MotionEvent.Idle
    // When touch is initiated state changes to MotionEvent.Down, when pointer is moved MotionEvent.Move,
    // after removing pointer we go to MotionEvent.Up to conclude drawing and then to MotionEvent.Idle
    // to not have undesired behavior when this composable recomposes. Leaving state at MotionEvent.Up
    // causes incorrect drawing.
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    // This is our motion event we get from touch motion
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    // This is previous motion event before next touch is saved into this current position
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

    // Path is what is used for drawing line on Canvas
    val path = remember { Path() }

    val paint = remember {
        Paint().apply {
            textSize = 40f
            color = Color.Black.toArgb()
        }
    }

    val drawModifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .clipToBounds()
        .background(Color.White)
        .pointerMotionEvents(
            onDown = { pointerInputChange: PointerInputChange ->
                currentPosition = pointerInputChange.position
                motionEvent = MotionEvent.Down
                pointerInputChange.consume()
            },
            onMove = { pointerInputChange: PointerInputChange ->
                currentPosition = pointerInputChange.position
                motionEvent = MotionEvent.Move
                pointerInputChange.consume()
            },
            onUp = { pointerInputChange: PointerInputChange ->
                motionEvent = MotionEvent.Up
                pointerInputChange.consume()
            },
            delayAfterDownInMillis = 25L
        )

    Canvas(modifier = drawModifier) {


        when (motionEvent) {
            MotionEvent.Down -> {
                path.moveTo(currentPosition.x, currentPosition.y)
                previousPosition = currentPosition
            }

            MotionEvent.Move -> {
                path.quadraticBezierTo(
                    previousPosition.x,
                    previousPosition.y,
                    (previousPosition.x + currentPosition.x) / 2,
                    (previousPosition.y + currentPosition.y) / 2

                )
                previousPosition = currentPosition
            }

            MotionEvent.Up -> {
                path.lineTo(currentPosition.x, currentPosition.y)
                currentPosition = Offset.Unspecified
                previousPosition = currentPosition
                motionEvent = MotionEvent.Idle
            }

            else -> Unit
        }

        drawPath(
            color = Color.Red,
            path = path,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}


@Composable
private fun PointerMotionEventListSample() {

    val paint = Paint().apply {
        textSize = 40f
        color = Color.Black.toArgb()
    }

    var text by remember {
        mutableStateOf("")
    }


    val drawModifier = canvasModifier
        .background(Color.White)
        .pointerMotionEventList(

            onDown = { pointerInputChange: PointerInputChange ->
                pointerInputChange.consume()
                text = "onDOWN id: ${pointerInputChange.id}\n" +
                        "pressed: ${pointerInputChange.pressed}, " +
                        "previousPressed: ${pointerInputChange.previousPressed}\n" +
                        "changedToDown: ${pointerInputChange.changedToDown()}\n" +
                        "changedToDownIgnoreConsumed: " +
                        "${pointerInputChange.changedToDownIgnoreConsumed()}\n" +
                        "isConsumed: ${pointerInputChange.isConsumed}\n"

            },
            onMove = {
                var pointerChangeText = "onMOVE Pointer size: ${it.size}\n"
                it.map { pointerInputChange: PointerInputChange ->
                    // Consuming change causes positionChange to return Offset.Zero
                    // and positionChanged() to false, uncomment it to see
                    pointerInputChange.consume()
                    pointerChangeText += "id: ${pointerInputChange.id}\n" +
                            " pressed: ${pointerInputChange.pressed}, " +
                            "previousPressed: ${pointerInputChange.previousPressed}\n" +
                            "isConsumed: ${pointerInputChange.isConsumed}\n" +
                            "positionChange: ${pointerInputChange.positionChange()}\n" +
                            "positionChanged: ${pointerInputChange.positionChanged()}\n" +
                            "pos: ${pointerInputChange.position}\n"
                }

                text = pointerChangeText
            },

            delayAfterDownInMillis = 20
        )


    Canvas(modifier = drawModifier) {
        drawText(text = text, x = 0f, y = 50f, paint = paint)
    }
}


private fun DrawScope.drawText(text: String, x: Float, y: Float, paint: Paint) {

    val lines = text.split("\n")
    // 🔥🔥 There is not a built-in function as of 1.0.0
    // for drawing text so we get the native canvas to draw text and use a Paint object
    val nativeCanvas = drawContext.canvas.nativeCanvas

    lines.indices.withIndex().forEach { (posY, i) ->
        nativeCanvas.drawText(lines[i], x, posY * 40 + y, paint)
    }
}

private val canvasModifier = Modifier
    .padding(8.dp)
    .shadow(1.dp)
    .fillMaxWidth()
    .height(300.dp)
    .clipToBounds()
