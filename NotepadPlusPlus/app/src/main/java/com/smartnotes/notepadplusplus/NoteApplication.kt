package com.smartnotes.notepadplusplus

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.smartnotes.notepadplusplus.data.database.NoteDatabase
import com.smartnotes.notepadplusplus.data.repository.NoteRepository
import com.smartnotes.notepadplusplus.utils.PreferencesManager

class NoteApplication : Application() {

    val database: NoteDatabase by lazy { NoteDatabase.getDatabase(this) }
    val repository: NoteRepository by lazy { NoteRepository(database.noteDao()) }
    val preferencesManager: PreferencesManager by lazy { PreferencesManager(this) }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initializeAdMob()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Note Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for note reminders"
                enableVibration(true)
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    private fun initializeAdMob() {

        // Add your test device ID here to avoid accidental policy violations
        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(listOf("YOUR_DEVICE_ID"))
            .build()

        MobileAds.setRequestConfiguration(configuration)

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this) { }
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "note_reminders"
    }
}
