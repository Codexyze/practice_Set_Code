package com.example.exoplayernotification.data.Notification


import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import javax.inject.Inject
const val CHANNELID="channel_1"
const val CHANNELNAME="mediaChannel"
class NotificationManagerHelper @Inject constructor(private val exoPlayer: ExoPlayer,
                                             private val mediaSession: MediaSession,
                                             private val  context: Context) {
    init {
        buildNotificationChannel()
    }
    fun buildNotificationChannel(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNELID,
                CHANNELNAME,
                NotificationManager.IMPORTANCE_HIGH

            )
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }

    }

@OptIn(UnstableApi::class)
fun createAndReturnNotification(): Notification {
    val playerNotificationManager = PlayerNotificationManager.Builder(
        context,
        1,
        CHANNELID
    ).setSmallIconResourceId(androidx.media3.session.R.drawable.media3_notification_small_icon)
        .setStopActionIconResourceId(R.drawable.ic_menu_manage)
        .setMediaDescriptionAdapter(CustomMediaDescriptionAdapter())
        .build().apply {
            setPlayer(exoPlayer)

        }

    // 👇 Return a dummy notification just to call startForeground()
    // PlayerNotificationManager takes care of updating the actual one later
    val noti =NotificationCompat.Builder(context, CHANNELID)
        .setContentTitle("Loading...").build()
    return noti
}


}