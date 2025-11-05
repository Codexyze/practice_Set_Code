package com.nutrino.broadcastrecivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class StringReciver: BroadcastReceiver() {
    override fun onReceive(context:Context?, intent: Intent?) {
        if(intent?.getStringExtra("key")=="Akshay"){
            Toast.makeText(context, "Akshay", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "its not Akshay", Toast.LENGTH_SHORT).show()

        }

    }
}