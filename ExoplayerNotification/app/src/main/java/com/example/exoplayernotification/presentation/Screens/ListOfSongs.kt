package com.example.exoplayernotification.presentation.Screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exoplayernotification.data.MusicForeGround.MusicForeGround
import com.example.exoplayernotification.presentation.ViewModel.AudioControllerViewModel
import com.example.exoplayernotification.presentation.ViewModel.AudioViewModel

@Composable
fun ListOfSongs(audioViewModel: AudioViewModel = hiltViewModel(),
                audioControllerViewModel: AudioControllerViewModel=hiltViewModel()) {
    LaunchedEffect(Unit) {
        audioViewModel.getAllSong()
    }
    val context = LocalContext.current
    val getAllSongState = audioViewModel.getAllSongsState.collectAsState()
    if(getAllSongState.value.isLoading){
        CircularProgressIndicator()
    }else if (getAllSongState.value.data != null){
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(getAllSongState.value.data) { song ->
                    Column {
                        Text(text = song.title.toString())
                        Row {
                            Button(
                                onClick = {
                                    audioControllerViewModel.playMusic(songUri = song.path.toUri())
                                    val intent = Intent(context, MusicForeGround::class.java)
                                    context.startService(intent)

                                }
                            ) {
                                Text("Play")
                            }
                            Button(
                                onClick = {
                                    audioControllerViewModel.pauseMusic()

                                }
                            ) {
                                Text("Pause")
                            }
                            Button(
                                onClick = {
                                    audioControllerViewModel.stopMusic()

                                }
                            ) {
                                Text("Stop")
                            }
                        }

                    }


                }
            }

        }
    }else if (getAllSongState.value.error != null){
        Text(text = getAllSongState.value.error.toString())
    }else{
        Text("No Data")
    }

}
