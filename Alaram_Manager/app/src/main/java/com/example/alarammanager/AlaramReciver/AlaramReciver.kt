package com.example.alarammanager.AlaramReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.alarammanager.Constants.Constants

class AlaramReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
       val message = intent?.getStringExtra("${Constants.ALARAMTRIGGERED}") ?: return
        Log.d("ALARAM","triggered $message")
    }
}