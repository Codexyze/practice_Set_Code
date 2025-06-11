package com.example.proguardrules

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proguardrules.Fake.Fake
import com.example.proguardrules.RepoImpl.RepoImpl1
import com.example.proguardrules.RepoImpl.RepoImpl2
import com.example.proguardrules.ui.theme.ProguardRulesTheme

class MainActivity : ComponentActivity() {
    val fake = Fake(name = "XYYYY")
    val fakeRepo1 = RepoImpl1()
    val fakeRepo2 = RepoImpl2()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProguardRulesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(fake.name)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(fakeRepo1.fakeFunction())
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(fakeRepo2.fakeFunction())
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProguardRulesTheme {
        Greeting("Android")
    }
}