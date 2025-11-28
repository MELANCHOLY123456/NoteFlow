package com.example.noteflow;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM notes WHERE id = :id")
    void deleteNoteById(int id);

    @Query("DELETE FROM notes")
    void deleteAllNotes();

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%'")
    List<Note> searchNotes(String searchQuery);
}