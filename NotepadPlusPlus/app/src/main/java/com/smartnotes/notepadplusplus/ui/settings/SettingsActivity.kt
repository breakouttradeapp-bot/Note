package com.smartnotes.notepadplusplus.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.smartnotes.notepadplusplus.NoteApplication
import com.smartnotes.notepadplusplus.databinding.ActivitySettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        binding.toolbar.setNavigationOnClickListener { finish() }

        val prefs = (application as NoteApplication).preferencesManager

        // Load current values before setting listeners to avoid triggering them prematurely
        lifecycleScope.launch {
            val darkMode = prefs.isDarkMode.first()
            val appLock = prefs.isAppLockEnabled.first()

            binding.switchDarkMode.isChecked = darkMode
            binding.switchAppLock.isChecked = appLock

            // Set listeners only AFTER loading values
            binding.switchDarkMode.setOnCheckedChangeListener { _, checked ->
                lifecycleScope.launch {
                    prefs.setDarkMode(checked)
                    AppCompatDelegate.setDefaultNightMode(
                        if (checked) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }

            binding.switchAppLock.setOnCheckedChangeListener { _, checked ->
                lifecycleScope.launch {
                    prefs.setAppLockEnabled(checked)
                }
            }
        }
    }
}
