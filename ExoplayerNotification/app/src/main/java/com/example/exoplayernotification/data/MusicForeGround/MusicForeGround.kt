package com.example.exoplayernotification.data.MusicForeGround

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.os.Build
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.example.exoplayernotification.data.Notification.CHANNELID
import com.example.exoplayernotification.data.Notification.CHANNELNAME
import com.example.exoplayernotification.data.Notification.CustomMediaDescriptionAdapter
import com.example.exoplayernotification.data.Notification.NotificationManagerHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//
//class MusicForeGround: Service(){
//    override fun onBind(p0: Intent?): IBinder? {
//        TODO("Not yet implemented")
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//
//        return START_STICKY
//
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        stopSelf()
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        buildNotificationChannel()
//    }
//
//    fun buildNotificationChannel(){
//
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
//            val channel = NotificationChannel(
//                CHANNELID,
//                CHANNELNAME,
//                NotificationManager.IMPORTANCE_HIGH
//
//            )
//            val manager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//
//        }
//
//    }
//
//    @OptIn(UnstableApi::class)
//    fun createAndReturnNotification(exoPlayer: ExoPlayer): Notification {
//        val playerNotificationManager = PlayerNotificationManager.Builder(
//            this,
//            1,
//            CHANNELID
//        ).setSmallIconResourceId(androidx.media3.session.R.drawable.media3_notification_small_icon)
//            .setMediaDescriptionAdapter(CustomMediaDescriptionAdapter())
//            .build().apply {
//                setPlayer(exoPlayer)
//
//            }
//
//        // 👇 Return a dummy notification just to call startForeground()
//        // PlayerNotificationManager takes care of updating the actual one later
//        val noti =NotificationCompat.Builder(this, CHANNELID)
//            .setContentTitle("Loading...").build()
//        return noti
//    }
//}

//class MusicForeGround : Service() {
//    @Inject
//    lateinit var mediaSession: MediaSession
//
//    @Inject
//    lateinit var notificationManagerHelper: NotificationManagerHelper
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        buildNotificationChannel()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val notification = notificationManagerHelper.createAndReturnNotification()
//        startForeground(1, notification)
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        exoPlayer.release()
//        mediaSession.release()
//        stopSelf()
//        super.onDestroy()
//    }
//
//    private fun buildNotificationChannel() {
//        // Your existing channel setup
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
//            val channel = NotificationChannel(
//                CHANNELID,
//                CHANNELNAME,
//                NotificationManager.IMPORTANCE_HIGH
//
//            )
//            val manager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//
//        }
//
//    }
//}
@AndroidEntryPoint
class MusicForeGround : Service() {
    @Inject lateinit var notificationManagerHelper: NotificationManagerHelper
    @Inject lateinit var exoPlayer: ExoPlayer


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = notificationManagerHelper.createAndReturnNotification()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    private fun buildNotificationChannel() {
        // Your existing channel setup
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNELID,
                CHANNELNAME,
                NotificationManager.IMPORTANCE_HIGH

            )
            val manager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }

    }
}