package com.nutrino.broadcastrecivers

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nutrino.broadcastrecivers.ui.theme.BroadCastReciversTheme

class MainActivity : ComponentActivity() {
    val airPlaneReciver = AirPlaneReciver()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BroadCastReciversTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, StringReciver::class.java).apply {
                                    this.putExtra("key","Akshay")
                                }
                                sendBroadcast(intent)

                            }
                        ) {
                            Text("Button ")
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airPlaneReciver, intentFilter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airPlaneReciver)
    }


}

