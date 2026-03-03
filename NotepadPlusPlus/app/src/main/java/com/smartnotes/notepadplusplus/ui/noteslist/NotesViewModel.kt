package com.smartnotes.notepadplusplus.ui.noteslist

import androidx.lifecycle.*
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.data.repository.NoteRepository
import com.smartnotes.notepadplusplus.utils.PreferencesManager
import com.smartnotes.notepadplusplus.utils.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val repository: NoteRepository,
    private val prefs: PreferencesManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _filterLabel = MutableStateFlow("")
    private val _showArchived = MutableStateFlow(false)

    val sortBy: StateFlow<String> = prefs.sortBy
        .stateIn(viewModelScope, SharingStarted.Lazily, "updated")

    val viewMode: StateFlow<String> = prefs.viewMode
        .stateIn(viewModelScope, SharingStarted.Lazily, "list")

    val allLabels: StateFlow<List<String>> = repository.getAllLabels()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val notesState: StateFlow<UiState<List<NoteEntity>>> = combine(
        _searchQuery.debounce(300),
        _filterLabel,
        _showArchived,
        sortBy
    ) { query, label, archived, sort ->
        Triple(query, label, archived to sort)
    }.flatMapLatest { (query, label, archivedSort) ->
        val (archived, sort) = archivedSort
        when {
            archived -> repository.getArchivedNotes()
            query.isNotBlank() -> repository.searchNotes(query)
            label.isNotBlank() -> repository.getNotesByLabel(label)
            else -> repository.getAllNotes(sort)
        }
    }.map { notes -> UiState.Success(notes) as UiState<List<NoteEntity>> }
        .catch { emit(UiState.Error(it.message ?: "Error loading notes")) }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    private val _actionState = MutableLiveData<UiState<String>>()
    val actionState: LiveData<UiState<String>> = _actionState

    private var lastDeletedNote: NoteEntity? = null

    fun setSearchQuery(q: String) { _searchQuery.value = q }
    fun setFilterLabel(label: String) { _filterLabel.value = label }
    fun setShowArchived(show: Boolean) { _showArchived.value = show }

    fun setSortBy(value: String) { viewModelScope.launch { prefs.setSortBy(value) } }
    fun setViewMode(value: String) { viewModelScope.launch { prefs.setViewMode(value) } }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            lastDeletedNote = note
            repository.deleteNote(note)
            _actionState.value = UiState.Success("deleted")
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            lastDeletedNote?.let { repository.insertNote(it) }
            lastDeletedNote = null
        }
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            repository.setPinned(note.id, !note.isPinned)
            _actionState.value = UiState.Success(if (!note.isPinned) "pinned" else "unpinned")
        }
    }

    fun toggleArchive(note: NoteEntity) {
        viewModelScope.launch {
            repository.setArchived(note.id, !note.isArchived)
            _actionState.value = UiState.Success(if (!note.isArchived) "archived" else "unarchived")
        }
    }
}

class NotesViewModelFactory(
    private val repository: NoteRepository,
    private val prefs: PreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
