package com.smartnotes.notepadplusplus.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.smartnotes.notepadplusplus.receivers.ReminderReceiver

object ReminderUtils {

    fun scheduleReminder(context: Context, noteId: Long, noteTitle: String, triggerAt: Long) {
        cancelReminder(context, noteId)
        if (triggerAt <= System.currentTimeMillis()) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("note_id", noteId)
            putExtra("note_title", noteTitle)
        }
        val pi = PendingIntent.getBroadcast(
            context,
            noteId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        }
    }

    fun cancelReminder(context: Context, noteId: Long) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context,
            noteId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pi?.let {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(it)
            it.cancel()
        }
    }
}
