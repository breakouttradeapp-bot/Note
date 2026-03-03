package com.smartnotes.notepadplusplus.ui.noteslist

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.smartnotes.notepadplusplus.NoteApplication
import com.smartnotes.notepadplusplus.R
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.databinding.ActivityNotesListBinding
import com.smartnotes.notepadplusplus.ui.addedit.AddEditNoteActivity
import com.smartnotes.notepadplusplus.ui.backup.BackupRestoreActivity
import com.smartnotes.notepadplusplus.ui.detail.NoteDetailActivity
import com.smartnotes.notepadplusplus.ui.privacy.PrivacyPolicyActivity
import com.smartnotes.notepadplusplus.ui.privacy.TermsActivity
import com.smartnotes.notepadplusplus.ui.settings.SettingsActivity
import com.smartnotes.notepadplusplus.utils.ExportUtils
import com.smartnotes.notepadplusplus.utils.UiState
import com.smartnotes.notepadplusplus.utils.gone
import com.smartnotes.notepadplusplus.utils.visible
import kotlinx.coroutines.launch

class NotesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesListBinding
    private lateinit var adapter: NotesAdapter
    private var isGridMode = false

    private val viewModel: NotesViewModel by viewModels {
        val app = application as NoteApplication
        NotesViewModelFactory(app.repository, app.preferencesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)

        setupRecyclerView()
        setupSearch()
        setupFab()
        setupSwipeToDelete()
        setupToolbar()
        setupAdMob()
        observeAll()
    }

    private fun setupAdMob() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.inflateMenu(R.menu.menu_notes_list)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_view_toggle -> { toggleViewMode(); true }
                R.id.menu_sort       -> { showSortDialog(); true }
                R.id.menu_archived   -> { viewModel.setShowArchived(true); true }
                R.id.menu_backup     -> { startActivity(Intent(this, BackupRestoreActivity::class.java)); true }
                R.id.menu_settings   -> { startActivity(Intent(this, SettingsActivity::class.java)); true }
                R.id.menu_privacy    -> { startActivity(Intent(this, PrivacyPolicyActivity::class.java)); true }
                R.id.menu_terms      -> { startActivity(Intent(this, TermsActivity::class.java)); true }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = NotesAdapter(
            isGrid = isGridMode,
            onClick = { note -> openNote(note) },
            onLongClick = { note, view -> showNoteContextMenu(note, view) }
        )
        updateLayoutManager()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy > 12) binding.fabAddNote.shrink()
                else if (dy < -12) binding.fabAddNote.extend()
            }
        })
    }

    private fun updateLayoutManager() {
        binding.recyclerView.layoutManager =
            if (isGridMode) GridLayoutManager(this, 2)
            else LinearLayoutManager(this)
    }

    private fun toggleViewMode() {
        isGridMode = !isGridMode
        adapter.setGridMode(isGridMode)
        updateLayoutManager()
        viewModel.setViewMode(if (isGridMode) "grid" else "list")
        binding.toolbar.menu?.findItem(R.id.menu_view_toggle)?.setIcon(
            if (isGridMode) R.drawable.ic_list else R.drawable.ic_grid
        )
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
        })
    }

    private fun setupFab() {
        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }
    }

    private fun setupSwipeToDelete() {
        val swipe = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val pos = vh.adapterPosition

                // ✅ FIX APPLIED HERE
                if (pos == RecyclerView.NO_POSITION || pos < 0) return

                val note = adapter.currentList.getOrNull(pos) ?: return
                viewModel.deleteNote(note)
                Snackbar.make(binding.root, "Note deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") { viewModel.undoDelete() }
                    .setActionTextColor(resources.getColor(R.color.primary, theme))
                    .show()
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(binding.recyclerView)
    }

    private fun observeAll() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visible()
                            binding.layoutEmpty.gone()
                        }
                        is UiState.Success -> {
                            binding.progressBar.gone()
                            val notes = state.data
                            supportActionBar?.subtitle = "${notes.size} notes"
                            if (notes.isEmpty()) {
                                binding.recyclerView.gone()
                                binding.layoutEmpty.visible()
                            } else {
                                binding.layoutEmpty.gone()
                                binding.recyclerView.visible()
                                adapter.submitList(notes)
                            }
                        }
                        is UiState.Error -> {
                            binding.progressBar.gone()
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allLabels.collect { labels -> buildLabelChips(labels) }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewMode.collect { mode ->
                    val grid = mode == "grid"
                    if (grid != isGridMode) {
                        isGridMode = grid
                        adapter.setGridMode(isGridMode)
                        updateLayoutManager()
                    }
                }
            }
        }
    }

    private fun buildLabelChips(labels: List<String>) {
        binding.chipGroupLabels.removeAllViews()
        if (labels.isEmpty()) {
            binding.chipGroupLabels.gone()
            return
        }
        binding.chipGroupLabels.visible()

        val allChip = Chip(this).apply {
            text = "All"
            isCheckable = true
            isChecked = true
            setOnClickListener { viewModel.setFilterLabel("") }
        }
        binding.chipGroupLabels.addView(allChip)

        labels.forEach { label ->
            val chip = Chip(this).apply {
                text = label
                isCheckable = true
                setOnClickListener { viewModel.setFilterLabel(label) }
            }
            binding.chipGroupLabels.addView(chip)
        }
    }

    private fun showNoteContextMenu(note: NoteEntity, anchor: View) {
        val options = arrayOf(
            if (note.isPinned) "Unpin" else "📌 Pin to Top",
            if (note.isArchived) "Unarchive" else "📦 Archive",
            "✏️ Edit",
            "🔗 Share",
            "📄 Export as TXT",
            "📕 Export as PDF",
            "🗑️ Delete"
        )
        AlertDialog.Builder(this)
            .setTitle(note.title.take(40).ifBlank { "Note" })
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.togglePin(note)
                    1 -> viewModel.toggleArchive(note)
                    2 -> startActivity(
                        Intent(this, AddEditNoteActivity::class.java)
                            .putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.id)
                    )
                    3 -> ExportUtils.shareNoteText(this, note)
                    4 -> ExportUtils.exportAsTxt(this, note)
                    5 -> ExportUtils.exportAsPdf(this, note)
                    6 -> confirmDelete(note)
                }
            }.show()
    }

    private fun confirmDelete(note: NoteEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Delete \"${note.title.take(30)}\"? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ -> viewModel.deleteNote(note) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSortDialog() {
        val options = arrayOf("Last Updated", "Date Created", "Title A–Z")
        val keys = arrayOf("updated", "created", "title")
        AlertDialog.Builder(this)
            .setTitle("Sort Notes By")
            .setItems(options) { _, i -> viewModel.setSortBy(keys[i]) }
            .show()
    }

    private fun openNote(note: NoteEntity) {
        startActivity(
            Intent(this, NoteDetailActivity::class.java)
                .putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
        )
    }
}
