package com.example.alarammanager.AlaramReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.alarammanager.Constants.Constants
import com.example.alarammanager.NotificationHandler.createNotification

class AlaramReciver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
       val message = intent?.getStringExtra("${Constants.ALARAMTRIGGERED}") ?: return
        createNotification(message = message, context = context)
        Log.d("ALARAM","triggered $message")
    }
}