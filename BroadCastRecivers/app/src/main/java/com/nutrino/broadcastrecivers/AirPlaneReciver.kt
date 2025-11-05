package com.nutrino.broadcastrecivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirPlaneReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.AIRPLANE_MODE"){
            val isOn = intent.getBooleanExtra("state",false)
            val message = if(isOn) "Airplane mode on " else "its off"
            Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
        }else{
            //
        }

    }
}