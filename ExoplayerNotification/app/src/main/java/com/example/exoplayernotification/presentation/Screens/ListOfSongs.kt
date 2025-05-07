package com.example.exoplayernotification.presentation.Screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exoplayernotification.presentation.ViewModel.AudioViewModel

@Composable
fun ListOfSongs(audioViewModel: AudioViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        audioViewModel.getAllSong()
    }
    val getAllSongState = audioViewModel.getAllSongsState.collectAsState()
    if(getAllSongState.value.isLoading){
        CircularProgressIndicator()
    }else if (getAllSongState.value.data != null){
        getAllSongState.value.data.forEachIndexed {
            index, song ->
            Text(text = song.title.toString())
        }
    }else if (getAllSongState.value.error != null){
        Text(text = getAllSongState.value.error.toString())
    }else{
        Text("No Data")
    }

}