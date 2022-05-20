package com.smarttoolfactory.composegesture.demo

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.gesture.MotionEvent
import com.smarttoolfactory.gesture.pointerMotionEventList
import com.smarttoolfactory.gesture.pointerMotionEvents

@Composable
fun MoveMotionEventDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var motionEvent1 by remember {
            mutableStateOf(MotionEvent.Idle)
        }

        var motionEvent2 by remember {
            mutableStateOf(MotionEvent.Idle)
        }

        var text by remember {
            mutableStateOf("")
        }

        val canvasModifier1 = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.LightGray)
            .pointerMotionEvents(
                onDown = {
                    motionEvent1 = MotionEvent.Down
                    it.consume()
                },
                onMove = {
                    motionEvent1 = MotionEvent.Move
                    it.consume()
                },
                onUp = {
                    motionEvent1 = MotionEvent.Up
                },
                delayAfterDownInMillis = 0
            )

        val canvasModifier2 = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .height(200.dp)
            .pointerMotionEvents(
                onDown = {
                    motionEvent2 = MotionEvent.Down
                    it.consume()
                },
                onMove = {
                    motionEvent2 = MotionEvent.Move
                    it.consume()
                },
                onUp = {
                    motionEvent2 = MotionEvent.Up
                },
                delayAfterDownInMillis = 20
            )


        val canvasModifier3 = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .height(200.dp)
            .pointerMotionEventList(

                onDown = {pointerInputChange: PointerInputChange ->
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

        Canvas(modifier = canvasModifier1) {

            when (motionEvent1) {

                MotionEvent.Down -> {
                    drawRect(Color.Yellow)
                }
                MotionEvent.Move -> {
                    drawRect(Color.Green)
                }

                MotionEvent.Up -> {
                    drawRect(Color.Red)
                    motionEvent1 = MotionEvent.Idle
                }
                else -> Unit
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text("pointerMotionEvents with delay")
        Canvas(modifier = canvasModifier2) {

            when (motionEvent2) {

                MotionEvent.Down -> {
                    drawRect(Color.Yellow)
                }
                MotionEvent.Move -> {
                    drawRect(Color.Green)
                }

                MotionEvent.Up -> {
                    drawRect(Color.Red)
                    motionEvent2 = MotionEvent.Idle
                }
                else -> Unit
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text("pointerMotionEventList pointers")

        val paint = Paint().apply {
            textSize = 40f
            color = Color.Black.toArgb()
        }
        Canvas(modifier = canvasModifier3) {
            drawText(text = text, x = 0f, y = 50f, paint = paint)
        }
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