package com.smartnotes.notepadplusplus.ui.addedit

import androidx.lifecycle.*
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.data.repository.NoteRepository
import com.smartnotes.notepadplusplus.utils.RichTextUtils
import com.smartnotes.notepadplusplus.utils.UiState
import kotlinx.coroutines.launch

data class SaveResult(val noteId: Long)

class AddEditViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _noteState = MutableLiveData<UiState<NoteEntity?>>()
    val noteState: LiveData<UiState<NoteEntity?>> = _noteState

    private val _saveState = MutableLiveData<UiState<SaveResult>>()
    val saveState: LiveData<UiState<SaveResult>> = _saveState

    private var originalNote: NoteEntity? = null

    var currentColor: String = "#FFFFFF"
    var currentLabel: String = ""
    var reminderTime: Long = 0L
    var isLocked: Boolean = false

    fun loadNote(noteId: Long) {
        if (noteId <= 0L) {
            _noteState.value = UiState.Success(null)
            return
        }
        viewModelScope.launch {
            _noteState.value = UiState.Loading
            val note = repository.getNoteById(noteId)
            originalNote = note
            note?.let {
                currentColor  = it.color
                currentLabel  = it.label
                reminderTime  = it.reminderTime
                isLocked      = it.isLocked
            }
            _noteState.value = UiState.Success(note)
        }
    }

    fun saveNote(title: String, content: String) {
        val trimTitle = title.trim()
        if (trimTitle.isBlank()) {
            _saveState.value = UiState.Error("Title cannot be empty")
            return
        }
        viewModelScope.launch {
            _saveState.value = UiState.Loading
            val wordCount = RichTextUtils.countWords(content)
            val now = System.currentTimeMillis()
            if (originalNote != null) {
                val updated = originalNote!!.copy(
                    title = trimTitle,
                    content = content.trim(),
                    color = currentColor,
                    label = currentLabel,
                    isLocked = isLocked,
                    reminderTime = reminderTime,
                    wordCount = wordCount,
                    updatedAt = now
                )
                val result = repository.updateNote(updated)
                _saveState.value = if (result.isSuccess)
                    UiState.Success(SaveResult(originalNote!!.id))
                else
                    UiState.Error(result.exceptionOrNull()?.message ?: "Failed to update")
            } else {
                val newNote = NoteEntity(
                    title = trimTitle,
                    content = content.trim(),
                    color = currentColor,
                    label = currentLabel,
                    isLocked = isLocked,
                    reminderTime = reminderTime,
                    wordCount = wordCount
                )
                val result = repository.insertNote(newNote)
                _saveState.value = if (result.isSuccess)
                    UiState.Success(SaveResult(result.getOrDefault(-1L)))
                else
                    UiState.Error(result.exceptionOrNull()?.message ?: "Failed to save")
            }
        }
    }

    fun hasUnsavedChanges(title: String, content: String): Boolean {
        return if (originalNote == null) {
            title.isNotBlank() || content.isNotBlank()
        } else {
            title.trim() != originalNote!!.title || content.trim() != originalNote!!.content ||
            currentColor != originalNote!!.color || currentLabel != originalNote!!.label
        }
    }
}

class AddEditViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
