package com.example.noteflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<NoteItem> notes;

    public NoteAdapter(List<NoteItem> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteItem note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.previewTextView.setText(note.getPreview());
        holder.timestampTextView.setText(note.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView previewTextView;
        TextView timestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title);
            previewTextView = itemView.findViewById(R.id.note_preview);
            timestampTextView = itemView.findViewById(R.id.note_timestamp);
        }
    }

    // 创建示例数据
    public static List<NoteItem> createSampleNotes(Context context) {
        List<NoteItem> notes = new ArrayList<>();
        notes.add(new NoteItem(
            context.getString(R.string.first_article_title), 
            context.getString(R.string.first_article_preview), 
            "2025-01-15"
        ));
        notes.add(new NoteItem(
            context.getString(R.string.learning_notes_title), 
            context.getString(R.string.learning_notes_preview), 
            "2025-01-14"
        ));
        notes.add(new NoteItem(
            context.getString(R.string.life_reflection_title), 
            context.getString(R.string.life_reflection_preview), 
            "2025-01-13"
        ));
        notes.add(new NoteItem(
            context.getString(R.string.tech_share_title), 
            context.getString(R.string.tech_share_preview), 
            "2025-01-12"
        ));
        notes.add(new NoteItem(
            context.getString(R.string.travel_diary_title), 
            context.getString(R.string.travel_diary_preview), 
            "2025-01-11"
        ));
        return notes;
    }
}