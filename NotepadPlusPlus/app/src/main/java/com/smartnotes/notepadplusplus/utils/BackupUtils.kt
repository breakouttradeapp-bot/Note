package com.smartnotes.notepadplusplus.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.smartnotes.notepadplusplus.data.database.NoteEntity

object BackupUtils {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    data class BackupFile(
        val version: Int = 2,
        val exportedAt: Long = System.currentTimeMillis(),
        val noteCount: Int,
        val notes: List<NoteEntity>
    )

    fun toJson(notes: List<NoteEntity>): String {
        val backup = BackupFile(noteCount = notes.size, notes = notes)
        return gson.toJson(backup)
    }

    fun fromJson(json: String): List<NoteEntity>? {
        return try {
            val backup = gson.fromJson(json, BackupFile::class.java)
            backup?.notes?.map { it.copy(id = 0) } // reset IDs to avoid conflicts
        } catch (e: Exception) {
            // Try legacy format (plain list)
            try {
                val type = object : TypeToken<List<NoteEntity>>() {}.type
                val list: List<NoteEntity> = gson.fromJson(json, type)
                list.map { it.copy(id = 0) }
            } catch (e2: Exception) {
                null
            }
        }
    }
}
