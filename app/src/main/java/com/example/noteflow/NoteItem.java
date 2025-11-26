package com.example.noteflow;

public class NoteItem {
    private String title;
    private String preview;
    private String timestamp;

    public NoteItem(String title, String preview, String timestamp) {
        this.title = title;
        this.preview = preview;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}