package com.smartnotes.notepadplusplus.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "content_html") val contentHtml: String = "",   // Rich text HTML

    // Categorisation
    @ColumnInfo(name = "color") val color: String = "#FFFFFF",        // Hex card color
    @ColumnInfo(name = "label") val label: String = "",               // Single label/tag

    // Status
    @ColumnInfo(name = "is_pinned") val isPinned: Boolean = false,
    @ColumnInfo(name = "is_locked") val isLocked: Boolean = false,    // Password protected
    @ColumnInfo(name = "is_archived") val isArchived: Boolean = false,

    // Reminder
    @ColumnInfo(name = "reminder_time") val reminderTime: Long = 0L,  // 0 = no reminder

    // Stats
    @ColumnInfo(name = "word_count") val wordCount: Int = 0,

    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)
