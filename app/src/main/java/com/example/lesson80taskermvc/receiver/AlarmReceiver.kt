package com.example.lesson80taskermvc.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val alarmMedia = RingtoneManager.getRingtone(context, notification)
        Toast.makeText(context, "Alarm has started", Toast.LENGTH_SHORT).show()
        alarmMedia.play()
    }
}