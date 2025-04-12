package com.example.intents

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.intents.ui.theme.IntentsTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Makes content draw behind the system bars
        enableEdgeToEdge()

        // Setting up the Compose content
        setContent {
            IntentsTheme { // Custom MaterialTheme
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Box used to apply innerPadding inside Scaffold
                    Box(modifier = Modifier.padding(innerPadding)) {

                        // 👇 Uncomment these to test different intent types
                        // openIntent()
                        // EmailOpener()
                        // OpenWebViewIntent()
                         // DialContact()
                       // OpenSecondActivity()

                    }
                }
            }
        }

    }
}


@Composable
fun  EmailOpener(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    fun emailIntent(){
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT,"Reaserach")
            putExtra(Intent.EXTRA_TEXT,"hello this is main content")
            putExtra(Intent.EXTRA_EMAIL,arrayOf("google@gmail.com"))
        }
        context.startActivity(intent)
    }
    Button(
        onClick = {
            emailIntent()
        }
    ) {
        Text("Emaail")
    }

}

@Composable
fun OpenWebViewIntent() {
    val context =LocalContext.current
    fun openWebViewIntent(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Codexyze/practice_Set_Code"))
        context.startActivity(intent)
    }
    openWebViewIntent()

}

@Composable
fun DialContact(modifier: Modifier = Modifier) {
   val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phoneNumber ="tel:7784777777"
                data = phoneNumber.toUri()
            }
           context.startActivity(intent)

        }
    ) {
        Text("Call")
    }

}

@Composable
fun OpenSecondActivity(modifier: Modifier = Modifier) {
    // 🔥 Explicit Intent Example — launch SecondActivity
    val context =LocalContext.current
    Button(
        onClick = {
            // Creating explicit intent to go to SecondActivity
            val intent = Intent(context, SecondActivity::class.java)
            context.startActivity(intent) // Launch the activity
        }
    ) {
        Text("Second activity") // Button text
    }
}

class SecondActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Spacer(modifier = Modifier.height(150.dp))
            Text("hello second Activity")
        } //Explicit intent type

    }

}