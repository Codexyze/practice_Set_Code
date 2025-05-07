package com.example.exoplayernotification.data.MediaController

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject

class MediaController @Inject constructor(private val exoPlayer: ExoPlayer, context: Context) {
    private val context = context
    fun playMusic(song: Uri){
        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(song))
            prepare()
            playWhenReady = true
        }
    }
    fun pauseMusic(){
       if(exoPlayer.isPlaying){
           exoPlayer.pause()
       }else{
           Toast.makeText(context, "Song is Not playing", Toast.LENGTH_SHORT).show()
       }
    }

    fun stopSong(){
        if(exoPlayer.isPlaying){
            exoPlayer.stop()
        }else{
            Toast.makeText(context, "Song is Not playing", Toast.LENGTH_SHORT).show()
        }
    }
}