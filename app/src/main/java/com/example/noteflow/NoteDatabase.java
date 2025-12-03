package com.example.noteflow;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class, Tag.class, NoteTag.class}, version = 2, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                NoteDatabase.class,
                "note_database"
            )
            .fallbackToDestructiveMigration() // 简单的迁移策略，实际应用中可能需要更复杂的迁移
            .build();
        }
        return instance;
    }
}