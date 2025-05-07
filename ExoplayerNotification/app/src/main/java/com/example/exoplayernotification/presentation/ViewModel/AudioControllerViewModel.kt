package com.example.exoplayernotification.presentation.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.exoplayernotification.data.MediaController.MediaController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AudioControllerViewModel @Inject constructor(private val mediaController: MediaController): ViewModel() {
    fun playMusic(songUri: Uri){
        mediaController.playMusic(song = songUri)
    }
    fun pauseMusic(){
        mediaController.pauseMusic( )
    }
    fun stopMusic(){
        mediaController.stopSong()
    }

}