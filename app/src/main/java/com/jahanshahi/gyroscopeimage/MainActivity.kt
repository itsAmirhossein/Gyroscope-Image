package com.jahanshahi.gyroscopeimage

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jahanshahi.gyroscopeimage.ui.theme.GyroscopeImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var rotationX by remember { mutableFloatStateOf(0f) }
            var rotationY by remember { mutableFloatStateOf(0f) }
            var translationX by remember { mutableFloatStateOf(0f) }
            var translationY by remember { mutableFloatStateOf(0f) }

            // Define rotation and translation limits and smoothing factors
            val maxRotation = 5f // Maximum rotation in degrees
            val maxTranslation = 100f // Maximum translation in pixels (adjust as needed)
            val smoothingFactor = 0.1f // Adjust this value for smoothing rotation and translation

            LaunchedEffect(Unit) {
                val sensorManager =
                    this@MainActivity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        if (event != null) {
                            val deltaX = event.values[0] // Rotation around x-axis
                            val deltaY = event.values[1] // Rotation around y-axis

                            // Smooth out the rotation values
                            rotationX = (rotationX + deltaX * 15 * smoothingFactor).coerceIn(
                                -maxRotation,
                                maxRotation
                            )
                            rotationY = (rotationY + deltaY * 15 * smoothingFactor).coerceIn(
                                -maxRotation,
                                maxRotation
                            )

                            // Smooth out the translation values
                            translationX = (translationX + deltaX * smoothingFactor * 15).coerceIn(
                                -maxTranslation,
                                maxTranslation
                            )
                            translationY = (translationY + deltaY * smoothingFactor * 15).coerceIn(
                                -maxTranslation,
                                maxTranslation
                            )
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                }
                sensorManager.registerListener(
                    listener,
                    gyroscopeSensor,
                    SensorManager.SENSOR_DELAY_GAME
                )
            }

            GyroscopeImageTheme {
                Box(
                    modifier = Modifier.fillMaxSize().background(color = Color(0xFF46080d)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_bg),
                        modifier = Modifier.fillMaxSize()
                            .graphicsLayer(
                            rotationX = rotationY,
                            rotationY = rotationX
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "CR7 Image",
                    )
                    Image(
                        painter = painterResource(id = R.drawable.img_fg),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 48.dp)
                            .zIndex(2f)
                            .graphicsLayer(
                                rotationX = rotationX,
                                rotationY = rotationY
                            ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "CR7 Image",
                    )
                }

            }
        }
    }
}
