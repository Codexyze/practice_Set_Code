package com.example.alarammanager.NotificationHandler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.alarammanager.Constants.Constants
import com.example.alarammanager.R

fun createNotificationChannel(context: Context){
    val notificationChannel = NotificationChannel(Constants.NOTIFICATIONCHANNELID, Constants.NOTIFICATIONCHANNELNAME, NotificationManager.IMPORTANCE_LOW)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationService =context.getSystemService(NotificationManager::class.java)
        notificationService.createNotificationChannel(notificationChannel)
    }
}
fun createNotification(message: String,context: Context){
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    val notification = NotificationCompat.Builder(context, Constants.NOTIFICATIONCHANNELID).setContentTitle("$message")
        .setSmallIcon(R.drawable.ic_launcher_background).build()
    notificationManager.notify(1,notification)

}