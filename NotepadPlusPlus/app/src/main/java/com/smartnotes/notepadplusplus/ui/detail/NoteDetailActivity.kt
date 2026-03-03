package com.smartnotes.notepadplusplus.ui.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.smartnotes.notepadplusplus.NoteApplication
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.databinding.ActivityNoteDetailBinding
import com.smartnotes.notepadplusplus.ui.addedit.AddEditNoteActivity
import com.smartnotes.notepadplusplus.utils.DateUtils
import com.smartnotes.notepadplusplus.utils.RichTextUtils
import com.smartnotes.notepadplusplus.utils.visible
import kotlinx.coroutines.launch

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    private lateinit var binding: ActivityNoteDetailBinding
    private var noteId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.fabEdit.setOnClickListener {
            startActivity(
                Intent(this, AddEditNoteActivity::class.java)
                    .putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, noteId)
            )
        }

        loadNote()
    }

    override fun onResume() {
        super.onResume()
        loadNote()
    }

    private fun loadNote() {
        lifecycleScope.launch {
            val repo = (application as NoteApplication).repository
            val note = repo.getNoteById(noteId)
            if (note == null) {
                finish()
                return@launch
            }
            supportActionBar?.title = note.title.take(30).ifBlank { "Note" }
            if (note.isLocked) {
                authenticateAndShow(note) { displayNote(note) }
            } else {
                displayNote(note)
            }
        }
    }

    private fun displayNote(note: NoteEntity) {
        binding.tvTitle.text = note.title.ifBlank { "Untitled" }
        binding.tvContent.text = note.content.ifBlank { "No content" }
        binding.tvDate.text = "Updated: ${DateUtils.formatDateTime(note.updatedAt)}"
        binding.tvWordCount.text = "${note.wordCount} words · ${RichTextUtils.estimateReadTime(note.wordCount)}"

        if (note.label.isNotBlank()) {
            binding.tvLabel.text = note.label
            binding.tvLabel.visibility = View.VISIBLE
        } else {
            binding.tvLabel.visibility = View.GONE
        }

        if (note.reminderTime > System.currentTimeMillis()) {
            binding.tvReminder.text = "⏰  Reminder: ${DateUtils.formatDateTime(note.reminderTime)}"
            binding.tvReminder.visible()
        } else {
            binding.tvReminder.visibility = View.GONE
        }

        try {
            val color = Color.parseColor(note.color)
            binding.appBarLayout.setBackgroundColor(color)
        } catch (e: Exception) { /* keep default color */ }
    }

    private fun authenticateAndShow(note: NoteEntity, onSuccess: () -> Unit) {
        val authenticators =
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL

        val canAuth = BiometricManager.from(this).canAuthenticate(authenticators)
        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            // No biometric / PIN available — show anyway
            onSuccess()
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Note")
            .setSubtitle(note.title.take(30).ifBlank { "Locked Note" })
            .setAllowedAuthenticators(authenticators)
            .build()

        val prompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        finish()
                    } else {
                        // Other errors (lockout etc.) — still close for safety
                        finish()
                    }
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Failed attempt — biometric prompt stays visible, no crash
                }
            }
        )
        prompt.authenticate(promptInfo)
    }
}
