package com.example.exoplayernotification.presentation.Screens

import android.media.audiofx.Equalizer
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

@OptIn(UnstableApi::class)
@Composable
fun MusicPlayerWithEqualizer() {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
            )
            prepare()
            play()
        }
    }

    var equalizer by remember { mutableStateOf<Equalizer?>(null) }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                equalizer?.release()
                equalizer = Equalizer(0, audioSessionId).apply {
                    enabled = true
                }
            }
        }
        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
            player.release()
            equalizer?.release()
        }
    }

    equalizer?.let { eq ->
        val bandCount = eq.numberOfBands.toInt()
        val minLevel = eq.bandLevelRange[0].toInt()
        val maxLevel = eq.bandLevelRange[1].toInt()

        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎧 Equalizer Settings", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            for (i in 0 until bandCount) {
                val band = i.toShort()
                val centerFreq = eq.getCenterFreq(band) / 1000 // Hz
                var level by remember { mutableStateOf(eq.getBandLevel(band).toInt()) }

                Text("Band ${i + 1} (${centerFreq}Hz)")
                Slider(
                    value = level.toFloat(),
                    onValueChange = {
                        level = it.toInt()
                        eq.setBandLevel(band, level.toShort())
                    },
                    valueRange = minLevel.toFloat()..maxLevel.toFloat(),
                    steps = 5,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
        }
    } ?: run {
        Text("⏳ Initializing Equalizer...", modifier = Modifier.padding(16.dp))
    }
}
