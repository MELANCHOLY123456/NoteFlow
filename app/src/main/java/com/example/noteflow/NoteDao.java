package com.example.noteflow;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Transaction;
import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNote(Note note);

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

    // 标签相关查询
    @Insert
    long insertTag(Tag tag);

    @Insert
    void insertNoteTag(NoteTag noteTag);

    @Query("SELECT * FROM tags WHERE tagName = :tagName")
    Tag getTagByName(String tagName);

    @Query("SELECT * FROM tags")
    List<Tag> getAllTags();

    @Query("DELETE FROM note_tags WHERE note_id = :noteId")
    void deleteNoteTagsForNote(int noteId);

    @Query("DELETE FROM note_tags WHERE note_id = :noteId AND tag_id = :tagId")
    void deleteNoteTag(int noteId, int tagId);

    @Query("SELECT * FROM tags WHERE id IN (SELECT tag_id FROM note_tags WHERE note_id = :noteId)")
    List<Tag> getTagsForNote(int noteId);

    @Transaction
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    List<NoteWithTags> getAllNotesWithTags();

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :noteId")
    NoteWithTags getNoteWithTags(int noteId);
}