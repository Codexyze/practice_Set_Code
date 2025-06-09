package com.example.alarammanager.AlaramHandler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.alarammanager.AlaramReciver.AlaramReciver
import com.example.alarammanager.Constants.Constants
import com.example.alarammanager.data.AlaramItem

class AlaramHandler{

    fun scheduleAlaram(alaramItem: AlaramItem,context: Context){
        val intent = Intent(context, AlaramReciver::class.java).apply {
            this.putExtra("${Constants.ALARAMTRIGGERED}",alaramItem.message)
        }
        val alaramManager = context.getSystemService(AlarmManager::class.java)
        alaramManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,alaramItem.time,
            PendingIntent.getBroadcast(context,45,intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        )
    }

}