package com.example.alarammanager

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.alarammanager.AlaramHandler.AlaramHandler
import com.example.alarammanager.data.AlaramItem
import com.example.alarammanager.ui.theme.AlaramManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlaramManagerTheme {
                AlarmSchedulerScreen(this)

            }
        }
    }
}
@Composable
fun AlarmSchedulerScreen(context: Context) {
    var message by remember { mutableStateOf("") }
    var timeInSeconds by remember { mutableStateOf("") }
    val alarmHandler = remember { AlaramHandler() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Set Alarm", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Message input
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Alarm Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time input (in seconds)
        OutlinedTextField(
            value = timeInSeconds,
            onValueChange = { timeInSeconds = it },
            label = { Text("Time in Seconds") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val seconds = timeInSeconds.toLongOrNull()
                if (seconds != null && message.isNotBlank()) {
                    val triggerTime = System.currentTimeMillis() + (seconds * 1000)
                    val alarmItem = AlaramItem(triggerTime, message)
                    alarmHandler.scheduleAlaram(alarmItem, context)
                    Toast.makeText(context, "Alarm Scheduled in $seconds seconds", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Enter valid message and time", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Schedule Alarm")
        }
    }
}

