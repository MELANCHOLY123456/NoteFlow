package com.example.noteflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private Context context;

    public NoteAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
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
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        
        // 显示正文的前两行
        String content = note.getContent();
        String firstTwoLines = getFirstTwoLines(content);
        holder.previewTextView.setText(firstTwoLines);
        
        holder.timestampTextView.setText(note.getTimestamp());
        
        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteDetailActivity.class);
            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("timestamp", note.getTimestamp());
            intent.putExtra("content", note.getContent());
            context.startActivity(intent);
        });
    }
    
    // 获取内容的前两行，不重复内容
    private String getFirstTwoLines(String content) {
        if (content == null || content.isEmpty()) {
            return "空白内容\n";
        }
        
        // 如果内容中包含换行符，取前两行
        String[] lines = content.split("\n", -1); // 使用-1以保留末尾空字符串
        
        if (lines.length >= 2) {
            // 返回前两行
            String result = lines[0];
            if (lines[1] != null) {
                result += "\n" + lines[1];
            } else {
                result += "\n";
            }
            return result;
        } else if (lines.length == 1) {
            // 如果只有一行，直接返回该行和一个空行，不重复内容
            String line = lines[0];
            if (line != null) {
                return line + "\n";
            } else {
                return "\n";
            }
        } else {
            return "\n"; // 返回空行
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
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
}