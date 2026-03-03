package com.smartnotes.notepadplusplus.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NoteDao_Impl implements NoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NoteEntity> __insertionAdapterOfNoteEntity;

  private final EntityDeletionOrUpdateAdapter<NoteEntity> __deletionAdapterOfNoteEntity;

  private final EntityDeletionOrUpdateAdapter<NoteEntity> __updateAdapterOfNoteEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfSetPinned;

  private final SharedSQLiteStatement __preparedStmtOfSetArchived;

  private final SharedSQLiteStatement __preparedStmtOfSetLocked;

  private final SharedSQLiteStatement __preparedStmtOfSetReminder;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public NoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNoteEntity = new EntityInsertionAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notes` (`id`,`title`,`content`,`content_html`,`color`,`label`,`is_pinned`,`is_locked`,`is_archived`,`reminder_time`,`word_count`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getContent());
        statement.bindString(4, entity.getContentHtml());
        statement.bindString(5, entity.getColor());
        statement.bindString(6, entity.getLabel());
        final int _tmp = entity.isPinned() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isLocked() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isArchived() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getReminderTime());
        statement.bindLong(11, entity.getWordCount());
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfNoteEntity = new EntityDeletionOrUpdateAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `notes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfNoteEntity = new EntityDeletionOrUpdateAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notes` SET `id` = ?,`title` = ?,`content` = ?,`content_html` = ?,`color` = ?,`label` = ?,`is_pinned` = ?,`is_locked` = ?,`is_archived` = ?,`reminder_time` = ?,`word_count` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getContent());
        statement.bindString(4, entity.getContentHtml());
        statement.bindString(5, entity.getColor());
        statement.bindString(6, entity.getLabel());
        final int _tmp = entity.isPinned() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isLocked() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isArchived() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getReminderTime());
        statement.bindLong(11, entity.getWordCount());
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetPinned = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET is_pinned = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetArchived = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET is_archived = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetLocked = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET is_locked = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetReminder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET reminder_time = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes";
        return _query;
      }
    };
  }

  @Override
  public Object insertNote(final NoteEntity note, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNoteEntity.insertAndReturnId(note);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<NoteEntity> notes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNoteEntity.insert(notes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteNote(final NoteEntity note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfNoteEntity.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNote(final NoteEntity note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNoteEntity.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setPinned(final long id, final boolean pinned,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetPinned.acquire();
        int _argIndex = 1;
        final int _tmp = pinned ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetPinned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setArchived(final long id, final boolean archived,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetArchived.acquire();
        int _argIndex = 1;
        final int _tmp = archived ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetArchived.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setLocked(final long id, final boolean locked,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetLocked.acquire();
        int _argIndex = 1;
        final int _tmp = locked ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetLocked.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setReminder(final long id, final long time,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetReminder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, time);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetReminder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NoteEntity>> getAllNotesByUpdated() {
    final String _sql = "SELECT * FROM notes WHERE is_archived = 0 ORDER BY is_pinned DESC, updated_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NoteEntity>> getAllNotesByCreated() {
    final String _sql = "SELECT * FROM notes WHERE is_archived = 0 ORDER BY is_pinned DESC, created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NoteEntity>> getAllNotesByTitle() {
    final String _sql = "SELECT * FROM notes WHERE is_archived = 0 ORDER BY is_pinned DESC, LOWER(title) ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NoteEntity>> getArchivedNotes() {
    final String _sql = "SELECT * FROM notes WHERE is_archived = 1 ORDER BY updated_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NoteEntity>> searchNotes(final String q) {
    final String _sql = "\n"
            + "        SELECT * FROM notes\n"
            + "        WHERE is_archived = 0\n"
            + "        AND (LOWER(title) LIKE '%' || LOWER(?) || '%' \n"
            + "             OR LOWER(content) LIKE '%' || LOWER(?) || '%')\n"
            + "        ORDER BY is_pinned DESC, updated_at DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, q);
    _argIndex = 2;
    _statement.bindString(_argIndex, q);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NoteEntity>> getNotesByLabel(final String label) {
    final String _sql = "SELECT * FROM notes WHERE is_archived = 0 AND label = ? ORDER BY is_pinned DESC, updated_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, label);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getNoteById(final long id, final Continuation<? super NoteEntity> $completion) {
    final String _sql = "SELECT * FROM notes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<NoteEntity>() {
      @Override
      @Nullable
      public NoteEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final NoteEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<String>> getAllLabels() {
    final String _sql = "SELECT DISTINCT label FROM notes WHERE label != '' AND is_archived = 0 ORDER BY LOWER(label) ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getNoteCount() {
    final String _sql = "SELECT COUNT(*) FROM notes WHERE is_archived = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllNotesForBackup(final Continuation<? super List<NoteEntity>> $completion) {
    final String _sql = "SELECT * FROM notes ORDER BY created_at ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfContentHtml = CursorUtil.getColumnIndexOrThrow(_cursor, "content_html");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "is_pinned");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfReminderTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reminder_time");
          final int _cursorIndexOfWordCount = CursorUtil.getColumnIndexOrThrow(_cursor, "word_count");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpContentHtml;
            _tmpContentHtml = _cursor.getString(_cursorIndexOfContentHtml);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final boolean _tmpIsPinned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
            _tmpIsPinned = _tmp != 0;
            final boolean _tmpIsLocked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsLocked);
            _tmpIsLocked = _tmp_1 != 0;
            final boolean _tmpIsArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp_2 != 0;
            final long _tmpReminderTime;
            _tmpReminderTime = _cursor.getLong(_cursorIndexOfReminderTime);
            final int _tmpWordCount;
            _tmpWordCount = _cursor.getInt(_cursorIndexOfWordCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpContentHtml,_tmpColor,_tmpLabel,_tmpIsPinned,_tmpIsLocked,_tmpIsArchived,_tmpReminderTime,_tmpWordCount,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
