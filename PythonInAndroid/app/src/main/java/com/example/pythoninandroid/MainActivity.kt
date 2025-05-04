package com.example.pythoninandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.pythoninandroid.ui.theme.PythonInAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PythonInAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        PythonText()

                    }

                }
            }
        }
    }
}
@Composable
fun PythonText() {
    var pythonOutput by remember { mutableStateOf("Loading...") }
    val context = LocalContext.current

    LaunchedEffect(true) {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }
        val py = Python.getInstance()
        val pyObj = py.getModule("script")
        pythonOutput = pyObj.callAttr("say_hello").toString()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = pythonOutput)
        Button(onClick = {
            val py = Python.getInstance()
            pythonOutput = py.getModule("script").callAttr("say_hello").toString()
        }) {
            Text("New joke")
        }
    }


}

