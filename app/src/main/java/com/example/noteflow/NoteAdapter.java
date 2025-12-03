package com.example.noteflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<NoteWithTags> notesWithTags;
    private Context context;

    public NoteAdapter(List<NoteWithTags> notesWithTags, Context context) {
        this.notesWithTags = notesWithTags;
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
        NoteWithTags noteWithTags = notesWithTags.get(position);
        Note note = noteWithTags.note;
        
        holder.titleTextView.setText(note.getTitle());
        
        // 显示正文的前两行
        String content = note.getContent();
        String firstTwoLines = getFirstTwoLines(content);
        holder.previewTextView.setText(firstTwoLines);
        
        holder.timestampTextView.setText(note.getTimestamp());
        
        // 清除之前的标签
        holder.tagsContainer.removeAllViews();
        
        // 添加标签
        if (noteWithTags.tags != null && !noteWithTags.tags.isEmpty()) {
            for (Tag tag : noteWithTags.tags) {
                // 创建标签视图
                TextView tagView = new TextView(context);
                tagView.setText(tag.getTagName());
                tagView.setTextSize(10);
                tagView.setPadding(12, 6, 12, 6);
                tagView.setTextColor(Color.WHITE);
                
                // 设置背景为圆角矩形
                tagView.setBackground(createTagBackground());
                
                // 添加边距
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(4, 0, 4, 0);
                tagView.setLayoutParams(params);
                
                // 添加到容器
                holder.tagsContainer.addView(tagView);
            }
        }
        
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
    
    // 创建标签背景
    private android.graphics.drawable.Drawable createTagBackground() {
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setColor(context.getResources().getColor(android.R.color.holo_blue_light));
        drawable.setCornerRadius(20f); // 设置圆角半径
        return drawable;
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
        return notesWithTags.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateNotes(List<NoteWithTags> newNotesWithTags) {
        this.notesWithTags = newNotesWithTags;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView previewTextView;
        TextView timestampTextView;
        LinearLayout tagsContainer;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title);
            previewTextView = itemView.findViewById(R.id.note_preview);
            timestampTextView = itemView.findViewById(R.id.note_timestamp);
            tagsContainer = itemView.findViewById(R.id.tags_container);
        }
    }
}