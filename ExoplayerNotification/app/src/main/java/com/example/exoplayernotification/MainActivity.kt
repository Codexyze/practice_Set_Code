package com.example.exoplayernotification

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.example.exoplayernotification.data.MusicForeGround.MusicForeGround
import com.example.exoplayernotification.presentation.Screens.ListOfSongs
import com.example.exoplayernotification.presentation.Screens.MusicPlayerWithEqualizer
import com.example.exoplayernotification.ui.theme.ExoplayerNotificationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExoplayerNotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                       // ListOfSongs()
                        MusicPlayerWithEqualizer()

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        val foreground = Intent(this, MusicForeGround::class.java)
        stopService(foreground)
        super.onDestroy()
    }
}
