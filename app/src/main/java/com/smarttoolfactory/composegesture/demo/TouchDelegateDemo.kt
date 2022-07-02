package com.smarttoolfactory.composegesture.demo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.composegesture.R
import com.smarttoolfactory.gesture.DelegateRect
import com.smarttoolfactory.gesture.touchDelegate

@Composable
fun TouchDelegateDemo() {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            modifier = Modifier
                .size(300.dp)
                .touchDelegate(
                    DelegateRect(
                        left = 30.dp,
                        right = 50.dp,
                        top = 40.dp,
                        bottom = 40.dp
                    ),
                    onClick = {
                        Toast
                            .makeText(context, "Clicked", Toast.LENGTH_SHORT)
                            .show()
                    }
                ),
            painter = painterResource(id = R.drawable.landscape),
            contentScale = ContentScale.FillBounds,
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(40.dp))


        Box(
            modifier = Modifier
                .size(300.dp)
                .border(2.dp, Color.Red)
                .touchDelegate(
                    DelegateRect(
                        left = (-30).dp,
                        right = (-40).dp,
                        top = (-30).dp,
                        bottom = (-35).dp
                    ),
                    onClick = {
                        Toast
                            .makeText(context, "Clicked", Toast.LENGTH_SHORT)
                            .show()
                    }
                ),
        )

    }
}