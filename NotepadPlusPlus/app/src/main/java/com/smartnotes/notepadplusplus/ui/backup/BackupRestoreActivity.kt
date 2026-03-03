package com.smartnotes.notepadplusplus.ui.backup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartnotes.notepadplusplus.NoteApplication
import com.smartnotes.notepadplusplus.databinding.ActivityBackupRestoreBinding
import com.smartnotes.notepadplusplus.utils.BackupUtils
import com.smartnotes.notepadplusplus.utils.DateUtils
import com.smartnotes.notepadplusplus.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class BackupRestoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackupRestoreBinding

    // Modern Activity Result API — no deprecated onActivityResult
    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { confirmRestore(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupRestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Backup & Restore"
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnExportBackup.setOnClickListener { exportBackup() }
        binding.btnImportBackup.setOnClickListener { pickFileLauncher.launch("application/json") }
    }

    private fun exportBackup() {
        lifecycleScope.launch {
            binding.btnExportBackup.isEnabled = false
            try {
                val repo = (application as NoteApplication).repository
                val notes = repo.getAllNotesForBackup()
                val json = BackupUtils.toJson(notes)
                val timestamp = DateUtils.formatDate(System.currentTimeMillis())
                    .replace(",", "").replace(" ", "_")
                val fileName = "notepad_backup_$timestamp.json"
                val file = File(cacheDir, fileName)
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { it.write(json.toByteArray()) }
                }
                val uri = androidx.core.content.FileProvider.getUriForFile(
                    this@BackupRestoreActivity, "${packageName}.fileprovider", file
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(intent, "Save Backup File"))
                binding.tvStatus.text = "✅ Exported ${notes.size} notes"
            } catch (e: Exception) {
                showToast("Export failed: ${e.message ?: "Unknown error"}")
            } finally {
                binding.btnExportBackup.isEnabled = true
            }
        }
    }

    private fun confirmRestore(uri: Uri) {
        AlertDialog.Builder(this)
            .setTitle("Restore Backup?")
            .setMessage("This will REPLACE all your current notes with the backup. This cannot be undone.\n\nContinue?")
            .setPositiveButton("Yes, Restore") { _, _ -> doRestore(uri) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun doRestore(uri: Uri) {
        lifecycleScope.launch {
            binding.btnImportBackup.isEnabled = false
            binding.tvStatus.text = "Restoring…"
            try {
                val json = withContext(Dispatchers.IO) {
                    contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                        ?: throw Exception("Could not read file")
                }
                val notes = BackupUtils.fromJson(json)
                    ?: throw Exception("Invalid backup file format")
                val repo = (application as NoteApplication).repository
                repo.restoreNotes(notes)
                binding.tvStatus.text = "✅ Restored ${notes.size} notes successfully"
            } catch (e: Exception) {
                binding.tvStatus.text = "❌ Restore failed"
                showToast("Restore failed: ${e.message ?: "Unknown error"}")
            } finally {
                binding.btnImportBackup.isEnabled = true
            }
        }
    }
}
