package com.example.exoplayernotification.data.MediaController

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.exoplayernotification.data.MusicForeGround.MusicForeGround
import com.example.exoplayernotification.data.Notification.NotificationManagerHelper
import javax.inject.Inject

class MediaController @Inject constructor(private val exoPlayer: ExoPlayer, context: Context,
    private val mediaSession: MediaSession,
    private  val notificationManagerHelper: NotificationManagerHelper):
    MediaSessionService() {
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

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return  mediaSession
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.stop()
        exoPlayer.release()
        mediaSession.release()
    }
}