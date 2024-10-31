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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val maxRotation = 20f // Maximum rotation in degrees
            val maxTranslation = 100f // Maximum translation in pixels (adjust as needed)
            val smoothingFactor = 0.2f // Adjust this value for smoothing rotation and translation

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
                            rotationX = (rotationX + deltaX * 10 * smoothingFactor).coerceIn(
                                -maxRotation,
                                maxRotation
                            )
                            rotationY = (rotationY + deltaY * 10 * smoothingFactor).coerceIn(
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.weight(8f).fillMaxSize().background(Color(0xFFDA020E)),
                        contentAlignment = Alignment.Center
                    ){
                        Card (
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                            modifier = Modifier
                                .size(200.dp)
                                .graphicsLayer(
                                    rotationX = rotationX,
                                    rotationY = rotationY
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.manutd),
                                modifier = Modifier
                                    .size(200.dp).padding(20.dp),
                                contentScale = ContentScale.Crop,
                                contentDescription = "CR7 Image",
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(2f).fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "rotationX: $rotationX",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "rotationY: $rotationY",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "translationX: $translationX",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "translationY: $translationY",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
