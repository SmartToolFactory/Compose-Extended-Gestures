package com.smarttoolfactory.composegesture

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.composegesture.ui.theme.ComposeGestureExtendedTheme
import com.smarttoolfactory.gesture.MotionEvent
import com.smarttoolfactory.gesture.pointerMotionEvents

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeGestureExtendedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        MoveEventDemo()
                    }
                }
            }
        }
    }
}

@Composable
private fun MoveEventDemo() {
    var motionEvent1 by remember {
        mutableStateOf(MotionEvent.Idle)
    }
    var offset1 by remember {
        mutableStateOf(Offset.Zero)
    }

    var motionEvent2 by remember {
        mutableStateOf(MotionEvent.Idle)
    }

    var offset2 by remember {
        mutableStateOf(Offset.Zero)
    }


    val canvasModifier1 = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .background(Color.LightGray)
        .pointerMotionEvents(
            onDown = {
                motionEvent1 = MotionEvent.Down
                offset1 = it.position
            },
            onMove = {
                motionEvent1 = MotionEvent.Move
                offset1 = it.position

            },
            onUp = {
                motionEvent1 = MotionEvent.Up
                offset1 = it.position
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
                offset2 = it.position
            },
            onMove = {
                motionEvent2 = MotionEvent.Move
                offset2 = it.position
            },
            onUp = {
                motionEvent2 = MotionEvent.Up
                offset2 = it.position
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

    Spacer(modifier = Modifier.height(10.dp))

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
