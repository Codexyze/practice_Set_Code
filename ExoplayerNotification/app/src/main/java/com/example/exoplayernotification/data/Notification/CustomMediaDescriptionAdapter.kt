package com.example.exoplayernotification.data.Notification

import android.app.PendingIntent
import android.graphics.Bitmap
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager

@UnstableApi
class CustomMediaDescriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        if(!player.mediaMetadata.title.isNullOrEmpty()){
            return player.mediaMetadata.title.toString()
        }else{
            return "Unknown Track"
        }
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
       return null
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return  null
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        return null
    }
}