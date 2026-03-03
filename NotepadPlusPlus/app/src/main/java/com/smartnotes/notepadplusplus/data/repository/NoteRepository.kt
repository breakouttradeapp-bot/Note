package com.smartnotes.notepadplusplus.data.repository

import com.smartnotes.notepadplusplus.data.database.NoteDao
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NoteRepository(private val dao: NoteDao) {

    fun getAllNotes(sortBy: String = "updated"): Flow<List<NoteEntity>> {
        val flow = when (sortBy) {
            "created" -> dao.getAllNotesByCreated()
            "title"   -> dao.getAllNotesByTitle()
            else      -> dao.getAllNotesByUpdated()
        }
        return flow.flowOn(Dispatchers.IO)
    }

    fun getArchivedNotes(): Flow<List<NoteEntity>> =
        dao.getArchivedNotes().flowOn(Dispatchers.IO)

    fun searchNotes(query: String): Flow<List<NoteEntity>> =
        dao.searchNotes(query.trim()).flowOn(Dispatchers.IO)

    fun getNotesByLabel(label: String): Flow<List<NoteEntity>> =
        dao.getNotesByLabel(label).flowOn(Dispatchers.IO)

    fun getAllLabels(): Flow<List<String>> =
        dao.getAllLabels().flowOn(Dispatchers.IO)

    fun getNoteCount(): Flow<Int> =
        dao.getNoteCount().flowOn(Dispatchers.IO)

    suspend fun getNoteById(id: Long): NoteEntity? = withContext(Dispatchers.IO) {
        runCatching { dao.getNoteById(id) }.getOrNull()
    }

    suspend fun insertNote(note: NoteEntity): Result<Long> = withContext(Dispatchers.IO) {
        runCatching { dao.insertNote(note) }
    }

    suspend fun updateNote(note: NoteEntity): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.updateNote(note) }
    }

    suspend fun deleteNote(note: NoteEntity): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.deleteNote(note) }
    }

    suspend fun deleteById(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.deleteById(id) }
    }

    suspend fun setPinned(id: Long, pinned: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.setPinned(id, pinned) }
    }

    suspend fun setArchived(id: Long, archived: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.setArchived(id, archived) }
    }

    suspend fun setLocked(id: Long, locked: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.setLocked(id, locked) }
    }

    suspend fun setReminder(id: Long, time: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dao.setReminder(id, time) }
    }

    suspend fun getAllNotesForBackup(): List<NoteEntity> = withContext(Dispatchers.IO) {
        runCatching { dao.getAllNotesForBackup() }.getOrDefault(emptyList())
    }

    suspend fun restoreNotes(notes: List<NoteEntity>): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            dao.deleteAll()
            dao.insertAll(notes)
        }
    }
}
