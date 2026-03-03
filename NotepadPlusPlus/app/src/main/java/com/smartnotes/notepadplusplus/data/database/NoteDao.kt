package com.smartnotes.notepadplusplus.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Three explicit sort queries — Room cannot parameterise ORDER BY safely
    @Query("SELECT * FROM notes WHERE is_archived = 0 ORDER BY is_pinned DESC, updated_at DESC")
    fun getAllNotesByUpdated(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_archived = 0 ORDER BY is_pinned DESC, created_at DESC")
    fun getAllNotesByCreated(): Flow<List<NoteEntity>>

    // FIXED: replaced COLLATE NOCASE with LOWER(title)
    @Query("SELECT * FROM notes WHERE is_archived = 0 ORDER BY is_pinned DESC, LOWER(title) ASC")
    fun getAllNotesByTitle(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_archived = 1 ORDER BY updated_at DESC")
    fun getArchivedNotes(): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes
        WHERE is_archived = 0
        AND (LOWER(title) LIKE '%' || LOWER(:q) || '%' 
             OR LOWER(content) LIKE '%' || LOWER(:q) || '%')
        ORDER BY is_pinned DESC, updated_at DESC
    """)
    fun searchNotes(q: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_archived = 0 AND label = :label ORDER BY is_pinned DESC, updated_at DESC")
    fun getNotesByLabel(label: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    // Also made label ordering case-insensitive safely
    @Query("SELECT DISTINCT label FROM notes WHERE label != '' AND is_archived = 0 ORDER BY LOWER(label) ASC")
    fun getAllLabels(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM notes WHERE is_archived = 0")
    fun getNoteCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE notes SET is_pinned = :pinned WHERE id = :id")
    suspend fun setPinned(id: Long, pinned: Boolean)

    @Query("UPDATE notes SET is_archived = :archived WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean)

    @Query("UPDATE notes SET is_locked = :locked WHERE id = :id")
    suspend fun setLocked(id: Long, locked: Boolean)

    @Query("UPDATE notes SET reminder_time = :time WHERE id = :id")
    suspend fun setReminder(id: Long, time: Long)

    @Query("SELECT * FROM notes ORDER BY created_at ASC")
    suspend fun getAllNotesForBackup(): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<NoteEntity>)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}
