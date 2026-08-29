package com.nutrino.worddocstools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nutrino.worddocstools.presentation.navigation.DocWorldNavHost
import com.nutrino.worddocstools.ui.theme.WordDocsToolsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordDocsToolsTheme {
                DocWorldNavHost()
            }
        }
    }
}
