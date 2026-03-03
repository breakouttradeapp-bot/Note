package com.smartnotes.notepadplusplus.ui.addedit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.smartnotes.notepadplusplus.NoteApplication
import com.smartnotes.notepadplusplus.R
import com.smartnotes.notepadplusplus.databinding.ActivityAddEditNoteBinding
import com.smartnotes.notepadplusplus.utils.DateUtils
import com.smartnotes.notepadplusplus.utils.NoteColors
import com.smartnotes.notepadplusplus.utils.ReminderUtils
import com.smartnotes.notepadplusplus.utils.RichTextUtils
import com.smartnotes.notepadplusplus.utils.UiState
import com.smartnotes.notepadplusplus.utils.gone
import com.smartnotes.notepadplusplus.utils.showToast
import com.smartnotes.notepadplusplus.utils.visible
import java.util.Calendar

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    private lateinit var binding: ActivityAddEditNoteBinding
    private val viewModel: AddEditViewModel by viewModels {
        AddEditViewModelFactory((application as NoteApplication).repository)
    }
    private var noteId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)

        setupToolbar()
        setupBackPress()
        setupSaveButton()
        setupColorPicker()
        setupLabelPicker()
        setupRichTextToolbar()
        setupReminderButton()
        observeViewModel()

        // Only load from DB once; ViewModel retains data across rotation
        if (savedInstanceState == null) {
            viewModel.loadNote(noteId)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (noteId > 0) getString(R.string.edit_note) else getString(R.string.add_note)
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }
    }

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { handleBackPress() }
        })
    }

    private fun handleBackPress() {
        val title = binding.etTitle.text?.toString() ?: ""
        val content = binding.etContent.text?.toString() ?: ""
        if (viewModel.hasUnsavedChanges(title, content)) {
            AlertDialog.Builder(this)
                .setTitle(R.string.unsaved_changes)
                .setMessage(R.string.unsaved_changes_message)
                .setPositiveButton(R.string.discard) { _, _ -> finish() }
                .setNegativeButton(R.string.keep_editing, null)
                .show()
        } else {
            finish()
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            // Clear any previous title error
            binding.tilTitle.error = null
            viewModel.saveNote(
                binding.etTitle.text?.toString() ?: "",
                binding.etContent.text?.toString() ?: ""
            )
        }
    }

    private fun setupColorPicker() {
        NoteColors.COLORS.forEach { hex ->
            val sizePx = dpToPx(40)
            val params = LinearLayout.LayoutParams(sizePx, sizePx).apply { marginEnd = dpToPx(8) }
            val colorView = View(this).apply {
                layoutParams = params
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    try { setColor(Color.parseColor(hex)) } catch (e: Exception) { setColor(Color.WHITE) }
                    setStroke(dpToPx(2), Color.parseColor("#DDDDDD"))
                }
                setOnClickListener {
                    viewModel.currentColor = hex
                    applyColorToToolbar(hex)
                }
            }
            binding.colorPickerRow.addView(colorView)
        }
    }

    private fun applyColorToToolbar(hex: String) {
        try {
            binding.toolbar.setBackgroundColor(Color.parseColor(hex))
        } catch (e: Exception) { /* ignore invalid hex */ }
    }

    private fun setupLabelPicker() {
        NoteColors.LABELS.forEach { label ->
            val chip = Chip(this).apply {
                text = label
                isCheckable = true
                setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        viewModel.currentLabel = label
                    } else if (viewModel.currentLabel == label) {
                        viewModel.currentLabel = ""
                    }
                }
            }
            binding.chipGroupLabel.addView(chip)
        }
    }

    private fun setupRichTextToolbar() {
        binding.btnBold.setOnClickListener { applyMarkdown("**", "**") }
        binding.btnItalic.setOnClickListener { applyMarkdown("_", "_") }
        binding.btnBullet.setOnClickListener {
            val pos = binding.etContent.selectionStart.coerceAtLeast(0)
            val text = binding.etContent.text?.toString() ?: ""
            val bullet = if (pos == 0 || text.getOrNull(pos - 1) == '\n') "- " else "\n- "
            val newText = text.substring(0, pos) + bullet + text.substring(pos)
            binding.etContent.setText(newText)
            binding.etContent.setSelection((pos + bullet.length).coerceAtMost(newText.length))
        }

        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val wc = RichTextUtils.countWords(s?.toString() ?: "")
                binding.tvWordCount.text = "$wc words · ${RichTextUtils.estimateReadTime(wc)}"
            }
        })
    }

    private fun applyMarkdown(prefix: String, suffix: String) {
        val start = binding.etContent.selectionStart.coerceAtLeast(0)
        val end = binding.etContent.selectionEnd.coerceAtLeast(start)
        if (start == end) { showToast("Select text first"); return }
        val text = binding.etContent.text?.toString() ?: ""
        val newText = text.substring(0, start) + prefix + text.substring(start, end) + suffix + text.substring(end)
        binding.etContent.setText(newText)
        binding.etContent.setSelection((end + prefix.length + suffix.length).coerceAtMost(newText.length))
    }

    private fun setupReminderButton() {
        binding.btnReminder.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                TimePickerDialog(this, { _, h, min ->
                    cal.set(y, m, d, h, min, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    val ms = cal.timeInMillis
                    if (ms <= System.currentTimeMillis()) {
                        showToast("Please select a future time")
                    } else {
                        viewModel.reminderTime = ms
                        binding.tvReminderSet.text = "⏰  ${DateUtils.formatDateTime(ms)}"
                        binding.tvReminderSet.visible()
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.btnReminder.setOnLongClickListener {
            viewModel.reminderTime = 0L
            binding.tvReminderSet.gone()
            showToast("Reminder cleared")
            true
        }
    }

    private fun observeViewModel() {
        viewModel.noteState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> binding.progressBar.visible()
                is UiState.Success -> {
                    binding.progressBar.gone()
                    state.data?.let { note ->
                        binding.etTitle.setText(note.title)
                        binding.etContent.setText(note.content)
                        val titleLen = note.title.length.coerceAtMost(binding.etTitle.text?.length ?: 0)
                        binding.etTitle.setSelection(titleLen)
                        applyColorToToolbar(note.color)
                        if (note.reminderTime > System.currentTimeMillis()) {
                            binding.tvReminderSet.text = "⏰  ${DateUtils.formatDateTime(note.reminderTime)}"
                            binding.tvReminderSet.visible()
                        }
                        for (i in 0 until binding.chipGroupLabel.childCount) {
                            val chip = binding.chipGroupLabel.getChildAt(i) as? Chip
                            chip?.isChecked = chip?.text?.toString() == note.label
                        }
                    }
                }
                is UiState.Error -> {
                    binding.progressBar.gone()
                    showToast(state.message)
                }
            }
        }

        viewModel.saveState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visible()
                    binding.btnSave.isEnabled = false
                }
                is UiState.Success -> {
                    binding.progressBar.gone()
                    binding.btnSave.isEnabled = true
                    // Schedule reminder with the actual saved noteId
                    val savedId = state.data.noteId
                    val reminderMs = viewModel.reminderTime
                    if (savedId > 0 && reminderMs > System.currentTimeMillis()) {
                        val title = binding.etTitle.text?.toString() ?: "Note"
                        ReminderUtils.scheduleReminder(this, savedId, title, reminderMs)
                    }
                    finish()
                }
                is UiState.Error -> {
                    binding.progressBar.gone()
                    binding.btnSave.isEnabled = true
                    if (state.message.contains("Title", ignoreCase = true)) {
                        binding.tilTitle.error = state.message
                    } else {
                        showToast(state.message)
                    }
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()
}
