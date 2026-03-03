package com.smartnotes.notepadplusplus.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [NoteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Safely add new columns — ignore if already exists
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN content_html TEXT NOT NULL DEFAULT ''") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN color TEXT NOT NULL DEFAULT '#FFFFFF'") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN label TEXT NOT NULL DEFAULT ''") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN is_pinned INTEGER NOT NULL DEFAULT 0") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN is_locked INTEGER NOT NULL DEFAULT 0") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN is_archived INTEGER NOT NULL DEFAULT 0") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN reminder_time INTEGER NOT NULL DEFAULT 0") }
                runCatching { db.execSQL("ALTER TABLE notes ADD COLUMN word_count INTEGER NOT NULL DEFAULT 0") }
            }
        }

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration() // only triggers if migration path not found
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
