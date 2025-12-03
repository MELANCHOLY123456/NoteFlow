package com.example.noteflow;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

// NoteWithTags类用于查询笔记及其关联的标签
public class NoteWithTags {
    @Embedded
    public Note note;
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = @Junction(
            value = NoteTag.class,
            parentColumn = "note_id",
            entityColumn = "tag_id"
        ),
        entity = Tag.class
    )
    public List<Tag> tags;
}
