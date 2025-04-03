package com.example.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
//
//class MainActivity : ComponentActivity(), SensorEventListener {
//
//    private lateinit var sensorManager: SensorManager
//    private var accelerometer: Sensor? = null
//    private var stepCounter: Sensor? = null
//    private var proximitySensor: Sensor? = null
//    private var lightSensor: Sensor? = null
//
//    private var xValue by mutableStateOf(0f)
//    private var yValue by mutableStateOf(0f)
//    private var zValue by mutableStateOf(0f)
//    private var stepCount by mutableStateOf(0)
//    private var proximityValue by mutableStateOf(0f)
//    private var lightValue by mutableStateOf(0f)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SensorDisplay(
//                xValue = xValue,
//                yValue = yValue,
//                zValue = zValue,
//                stepCount = stepCount,
//                proximityValue = proximityValue,
//                lightValue = lightValue
//            )
//        }
//
//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        initializeSensors()
//        requestStepCounterPermission()
//    }
//
//    private fun initializeSensors() {
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
//        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
//    }
//
//    private fun requestStepCounterPermission() {
//        if (stepCounter != null && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 1)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        registerSensor(accelerometer)
//        registerSensor(stepCounter)
//        registerSensor(proximitySensor)
//        registerSensor(lightSensor)
//    }
//
//    private fun registerSensor(sensor: Sensor?) {
//        sensor?.let {
//            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        sensorManager.unregisterListener(this)
//    }
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        event?.let {
//            when (it.sensor.type) {
//                Sensor.TYPE_ACCELEROMETER -> {
//                    xValue = it.values[0]
//                    yValue = it.values[1]
//                    zValue = it.values[2]
//                }
//                Sensor.TYPE_STEP_COUNTER -> {
//                    stepCount = it.values[0].toInt()
//                }
//                Sensor.TYPE_PROXIMITY -> {
//                    proximityValue = it.values[0]
//                }
//                Sensor.TYPE_LIGHT -> {
//                    lightValue = it.values[0]
//                }
//            }
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        // Handle accuracy changes if needed
//    }
//}
//
//@Composable
//fun SensorDisplay(xValue: Float, yValue: Float, zValue: Float, stepCount: Int, proximityValue: Float, lightValue: Float) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        Spacer(modifier = Modifier.height(36.dp))
//        Text("Accelerometer X: $xValue")
//        Text("Accelerometer Y: $yValue")
//        Text("Accelerometer Z: $zValue")
//        Text("Step Count: $stepCount")
//        Text("Proximity: $proximityValue")
//        Text("Light: $lightValue")
//    }
//}
//class MainActivity : ComponentActivity() ,SensorEventListener{
//    private lateinit var sensorManger:SensorManager
//    private val acelerator:Sensor? =null
//    private var xValue by mutableStateOf(0f)
//    private var yValue by mutableStateOf(0f)
//    private var zValue by mutableStateOf(0f)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (acelerator != null) {
//            registerSensor(acelerator)
//        }
//    }
//private fun registerSensor(sensor:Sensor){
//    sensor.let {
//        sensorManger.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)
//    }
//}
//    private fun inatilizeSensor(sensor: Sensor){
//        getSystemService()
//    }
//    override fun onSensorChanged(event: SensorEvent?) {
//      event.let {
//          when(it?.sensor?.type){
//              Sensor.TYPE_ACCELEROMETER->{
//                  xValue = it.values[0]
//                  yValue=it.values[1]
//                  zValue=it.values[2]
//              }
//          }
//      }
//    }
//
//    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//       //Not Needed Yet
//    }
//}

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var stepCounter: Sensor? = null
    private var light:Sensor? = null

    private var xValue by mutableStateOf(0f)
    private var yValue by mutableStateOf(0f)
    private var zValue by mutableStateOf(0f)
    private var stepCount by mutableStateOf(0)
    private var lightValue by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SensorDisplay(xValue, yValue, zValue, stepCount,lightValue) }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onResume() {
        super.onResume()
       // accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        if(accelerometer != null){
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }else{
            Toast.makeText(this, "Phone dont have acceleratormeter", Toast.LENGTH_SHORT).show()
        }
        stepCounter?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        light?.let { sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    xValue = it.values[0]
                    yValue = it.values[1]
                    zValue = it.values[2]
                }
                Sensor.TYPE_STEP_COUNTER -> {stepCount = it.values[0].toInt()}
                Sensor.TYPE_LIGHT->{
                    lightValue = it.values[0]
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun SensorDisplay(xValue: Float, yValue: Float, zValue: Float, stepCount: Int,light:Float) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("X: $xValue, Y: $yValue, Z: $zValue")
        Text("Steps: $stepCount")
        Text("LightSensor : $light")
    }
}

