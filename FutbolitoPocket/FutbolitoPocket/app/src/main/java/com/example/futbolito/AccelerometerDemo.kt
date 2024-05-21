package com.example.futbolito

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState

@Composable
fun AccelerometerDemo() {
    // Variables de estado para llevar el puntaje superior e inferior
    var topScore by remember { mutableStateOf(0) }
    var bottomScore by remember { mutableStateOf(0) }

    // Verifica si el sensor de acelerómetro está disponible
    if (isAccelerometerSensorAvailable()) {
        // Obtiene los valores del sensor de acelerómetro
        val sensorValue by rememberAccelerometerSensorValueAsState()
        val (x, y, z) = sensorValue.value

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Futbol",
                fontSize = 24.sp,
                modifier = Modifier.padding(0.dp, 32.dp, 16.dp, 16.dp)
            )

            // Marcador
            Text(
                text = buildAnnotatedString {
                    append("Team 1: ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(topScore.toString())
                    }
                    append(" Team 2: ")
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append(bottomScore.toString())
                    }
                },
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            // Contenedor
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                //VARIABLES
                val width = constraints.maxWidth.toFloat()
                val height = constraints.maxHeight.toFloat()
                var center by remember { mutableStateOf(Offset(width / 2, height / 2)) }
                val orientation = LocalConfiguration.current.orientation
                val radius = with(LocalDensity.current) { 10.dp.toPx() }

                // Calcula la nueva posición del círculo basado en la orientación y valores del acelerómetro
                center = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Offset(
                        x = (center.x - x).coerceIn(radius, width - radius),
                        y = (center.y + y).coerceIn(radius, height - radius),
                    )
                } else {
                    Offset(
                        x = (center.x + y).coerceIn(radius, width - radius),
                        y = (center.y + x).coerceIn(radius, height - radius),
                    )
                }

                // Verifica si el círculo toca los límites superior o inferior y actualiza el marcador
                when {
                    center.y - radius <= 0 -> {
                        topScore += 1
                        center = Offset(width / 2, height / 2) // Reposiciona el círculo en el centro
                    }
                    center.y + radius >= height -> {
                        bottomScore += 1
                        center = Offset(width / 2, height / 2) // Reposiciona el círculo en el centro
                    }
                }

                // Imagen de fondo
                val image = ImageBitmap.imageResource(id = R.drawable.cancha)

                // Canvas para dibujar la imagen de fondo y el círculo
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawImage(
                        image = image,
                        dstSize = IntSize(width.toInt(), height.toInt())
                    )
                    drawCircle(
                        color = Color.White,
                        radius = radius,
                        center = center,
                    )
                }
            }
        }
    } else {
        Text(
            text = "Sensor de acelerómetro no disponible",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}