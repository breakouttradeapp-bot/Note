package com.smartnotes.notepadplusplus.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.smartnotes.notepadplusplus.NoteApplication
import com.smartnotes.notepadplusplus.utils.ReminderUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val app = context.applicationContext as NoteApplication
        CoroutineScope(Dispatchers.IO).launch {
            val notes = app.repository.getAllNotesForBackup()
            val now = System.currentTimeMillis()
            notes.filter { it.reminderTime > now }.forEach { note ->
                ReminderUtils.scheduleReminder(context, note.id, note.title, note.reminderTime)
            }
        }
    }
}
