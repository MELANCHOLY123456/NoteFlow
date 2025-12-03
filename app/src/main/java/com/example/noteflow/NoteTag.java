package com.example.noteflow;

import androidx.room.Entity;

@Entity(tableName = "note_tags", primaryKeys = {"note_id", "tag_id"})
public class NoteTag {
    private int note_id;
    private int tag_id;

    public NoteTag(int note_id, int tag_id) {
        this.note_id = note_id;
        this.tag_id = tag_id;
    }

    // Getters and Setters
    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
}